package com.bangon.kiosk.service;
import com.bangon.kiosk.entity.Product;import com.bangon.kiosk.entity.Category;import com.bangon.kiosk.dto.ApiDtos.Choices;
import org.junit.jupiter.api.Test;import java.math.BigDecimal;import static org.junit.jupiter.api.Assertions.*;
class PricingServiceTest {
 PricingService svc=new PricingService(); Product latte=new Product(new Category("Coffee"),"Latte",150,120,220,"",false);
 @Test void defaultMedium(){var q=svc.quote(latte,null);assertEquals(new BigDecimal("150.00"),q.price());assertEquals(120,q.caffeine());}
 @Test void largeAndAddons(){var q=svc.quote(latte,new Choices("large","oat",50,true,true,true));assertEquals(new BigDecimal("265.00"),q.price());assertEquals(209,q.caffeine());}
 @Test void rejectsUnknownSize(){assertThrows(IllegalArgumentException.class,()->svc.quote(latte,new Choices("giant","whole",100,false,false,false)));}
 @Test void pointsAndDiscount(){assertEquals(3,svc.earnedPoints(new BigDecimal("199.00")));assertEquals(new BigDecimal("20"),svc.discount(true,100));assertThrows(IllegalArgumentException.class,()->svc.discount(true,99));}
}
