package com.bangon.kiosk.service;
import com.bangon.kiosk.dto.ApiDtos.*;import com.bangon.kiosk.entity.*;import com.bangon.kiosk.repository.*;
import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;import java.math.*;import java.util.*;
@Service public class OrderService {
 private final ProductRepository products;private final CustomerRepository customers;private final OrderRepository orders;private final PaymentRepository payments;private final LoyaltyRepository loyalty;private final PricingService pricing;
 public OrderService(ProductRepository p,CustomerRepository c,OrderRepository o,PaymentRepository pay,LoyaltyRepository l,PricingService s){products=p;customers=c;orders=o;payments=pay;loyalty=l;pricing=s;}
 @Transactional public Order place(Checkout req,Customer logged){
  if(req.idempotencyKey().length()>100)throw new IllegalArgumentException("Idempotency key too long");
  Optional<Order> previous=orders.findByIdempotencyKey(req.idempotencyKey());
  if(previous.isPresent()){Order existing=previous.get();if(!Objects.equals(existing.customer==null?null:existing.customer.id,logged==null?null:logged.id))throw new IllegalArgumentException("Idempotency key already used");return existing;}
  if(!Set.of("Cash","Card","E-Wallet").contains(req.paymentMethod()))throw new IllegalArgumentException("Unsupported payment method");
  Customer user=logged==null?null:customers.lockById(logged.id).orElseThrow();
  if(req.redeemPoints()&&user==null)throw new IllegalArgumentException("Sign in to redeem points");
  Order order=new Order();order.orderNumber="B"+UUID.randomUUID().toString().replace("-","").substring(0,12).toUpperCase();order.idempotencyKey=req.idempotencyKey();order.customer=user;order.paymentMethod=req.paymentMethod();
  for(Line line:req.items()){
   if(line.quantity()<1||line.quantity()>30)throw new IllegalArgumentException("Invalid quantity");
   Product p=products.findById(line.productId()).orElseThrow(()->new IllegalArgumentException("Product not found"));
   PricingService.Quote q=pricing.quote(p,line.choices());OrderItem item=new OrderItem();item.order=order;item.product=p;item.name=p.name;item.quantity=line.quantity();item.unitPrice=q.price();item.caffeine=q.caffeine();item.calories=q.calories();item.size=q.size();item.milk=q.milk();item.sweetness=q.sweetness();item.extraShot=q.extraShot();item.caramel=q.caramel();item.cream=q.cream();order.items.add(item);
   order.subtotal=order.subtotal.add(q.price().multiply(BigDecimal.valueOf(line.quantity())));order.caffeine+=q.caffeine()*line.quantity();order.calories+=q.calories()*line.quantity();
  }
  if(order.items.size()>50)throw new IllegalArgumentException("Too many line items");
  order.discount=pricing.discount(req.redeemPoints(),user==null?0:user.loyaltyPoints);
  if(order.discount.compareTo(order.subtotal)>0)order.discount=order.subtotal;
  order.total=order.subtotal.subtract(order.discount);
  // Points are reserved/deducted only after a staff-verified payment; no fake card/e-wallet success.
  order.pointsRedeemed=req.redeemPoints()?100:0;order.pointsEarned=user==null?0:pricing.earnedPoints(order.total);
  orders.saveAndFlush(order);Payment payment=new Payment();payment.order=order;payment.method=req.paymentMethod();payments.save(payment);
  return order;
 }
 @Transactional public Order confirmPaid(Long orderId){
  Order order=orders.lockById(orderId).orElseThrow(()->new IllegalArgumentException("Order not found"));
  if(order.paymentStatus.equals("PAID"))return order;
  if(order.customer!=null){Customer user=customers.lockById(order.customer.id).orElseThrow();if(order.pointsRedeemed>0){if(user.loyaltyPoints<order.pointsRedeemed)throw new IllegalArgumentException("Loyalty balance changed; payment cannot be confirmed");user.loyaltyPoints-=order.pointsRedeemed;log(user,order,-order.pointsRedeemed,"REDEEM");}user.loyaltyPoints+=order.pointsEarned;log(user,order,order.pointsEarned,"EARN");}
  order.paymentStatus="PAID";order.status="CONFIRMED";Payment payment=payments.findByOrderId(order.id);payment.status="PAID";payment.confirmedAt=java.time.Instant.now();return order;
 }
 private void log(Customer c,Order o,int delta,String reason){LoyaltyTransaction t=new LoyaltyTransaction();t.customer=c;t.order=o;t.delta=delta;t.reason=reason;loyalty.save(t);}
 public Map<String,Object> view(Order o){
  List<Map<String,Object>> items=o.items.stream().map(i->{Map<String,Object> m=new LinkedHashMap<>();m.put("name",i.name);m.put("quantity",i.quantity);m.put("unitPrice",i.unitPrice);m.put("caffeine",i.caffeine);m.put("calories",i.calories);m.put("details",i.size==null?"1 item":i.size+" · "+i.milk+" milk · "+i.sweetness+"% sweet"+(i.extraShot?" · extra shot":"")+(i.caramel?" · caramel syrup":"")+(i.cream?" · whipped cream":""));return m;}).toList();
  Map<String,Object> m=new LinkedHashMap<>();m.put("id",o.id);m.put("orderNumber",o.orderNumber);m.put("status",o.status);m.put("paymentStatus",o.paymentStatus);m.put("paymentMethod",o.paymentMethod);m.put("subtotal",o.subtotal);m.put("discount",o.discount);m.put("total",o.total);m.put("pointsEarned",o.pointsEarned);m.put("pointsRedeemed",o.pointsRedeemed);m.put("caffeine",o.caffeine);m.put("calories",o.calories);m.put("createdAt",o.createdAt);m.put("items",items);return m;
 }
}
