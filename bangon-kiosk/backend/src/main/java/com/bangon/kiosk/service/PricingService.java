package com.bangon.kiosk.service;
import com.bangon.kiosk.entity.Product;import com.bangon.kiosk.dto.ApiDtos.Choices;
import org.springframework.stereotype.Service;import java.math.*;import java.util.*;
@Service public class PricingService {
 public record Quote(BigDecimal price,int caffeine,int calories,String size,String milk,int sweetness,boolean extraShot,boolean caramel,boolean cream){}
 public Quote quote(Product p,Choices c){
  if(p==null||!p.active)throw new IllegalArgumentException("Unavailable product");
  if(p.pastry){if(c!=null && (c.size()!=null||c.milk()!=null||c.sweetness()!=null||c.extraShot()||c.caramel()||c.cream()))throw new IllegalArgumentException("Pastries cannot be customized");return new Quote(p.price,p.caffeine,p.calories,null,null,0,false,false,false);}
  String size=c==null||c.size()==null?"medium":c.size();String milk=c==null||c.milk()==null?"whole":c.milk();int sweet=c==null||c.sweetness()==null?100:c.sweetness();
  if(!Set.of("small","medium","large").contains(size)||!Set.of("whole","oat","almond").contains(milk)||!Set.of(25,50,100).contains(sweet))throw new IllegalArgumentException("Invalid customization");
  boolean extra=c!=null&&c.extraShot(),caramel=c!=null&&c.caramel(),cream=c!=null&&c.cream();
  double price=p.price.doubleValue(), caffeine=p.caffeine,calories=p.calories;
  if(size.equals("small")){price-=15;caffeine*=.85;calories*=.85;}
  if(size.equals("large")){price+=25;caffeine*=1.2;calories*=1.2;}
  if(!milk.equals("whole"))price+=20;
  if(milk.equals("oat"))calories+=20;
  if(milk.equals("almond"))calories+=10;
  calories*=.72+.28*(sweet/100.0);
  if(extra){price+=30;caffeine+=65;calories+=5;}
  if(caramel){price+=20;calories+=60;}
  if(cream){price+=20;calories+=80;}
  return new Quote(BigDecimal.valueOf(Math.round(price)).setScale(2), (int)Math.round(caffeine),(int)Math.round(calories),size,milk,sweet,extra,caramel,cream);
 }
 public int earnedPoints(BigDecimal paid){return paid.divide(BigDecimal.valueOf(50),0,RoundingMode.DOWN).intValue();}
 public BigDecimal discount(boolean redeem,int points){if(redeem && points<100)throw new IllegalArgumentException("Insufficient loyalty points");return redeem?BigDecimal.valueOf(20):BigDecimal.ZERO;}
}
