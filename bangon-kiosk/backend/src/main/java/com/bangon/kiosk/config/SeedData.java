package com.bangon.kiosk.config;
import com.bangon.kiosk.entity.*;import com.bangon.kiosk.repository.*;import org.springframework.boot.CommandLineRunner;import org.springframework.context.annotation.*;
@Configuration public class SeedData {
 @Bean CommandLineRunner seed(CategoryRepository cats,ProductRepository products){return args->{
  for(String name:new String[]{"Coffee","Frappes","Tea","Pastries","Desserts"})if(!cats.existsByName(name))cats.save(new Category(name));
  Object[][] rows={
  {"Coffee","Caramel Latte",150,120,220,"https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?auto=format&fit=crop&w=900&q=82",false},
  {"Coffee","Spanish Latte",145,115,210,"https://images.unsplash.com/photo-1461023058943-07fcbe16d735?auto=format&fit=crop&w=900&q=82",false},
  {"Coffee","Americano",95,150,15,"https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?auto=format&fit=crop&w=900&q=82",false},
  {"Coffee","Cappuccino",125,120,120,"https://images.unsplash.com/photo-1572442388796-11668a67e53d?auto=format&fit=crop&w=900&q=82",false},
  {"Frappes","Mocha Frappe",175,95,390,"https://images.unsplash.com/photo-1511920170033-f8396924c348?auto=format&fit=crop&w=900&q=82",false},
  {"Frappes","Caramel Frappe",180,70,430,"https://budgetdelicious.com/wp-content/uploads/2021/02/caramel-frappe-17-of-21.jpg6",false},
  {"Tea","Matcha Latte",145,65,200,"https://loremflickr.com/900/700/matcha,latte?lock=707",false},
  {"Tea","Strawberry Tea Latte",135,35,190,"https://loremflickr.com/900/700/strawberry,drink,cafe?lock=808",false},
  {"Pastries","Butter Croissant",85,0,280,"https://images.unsplash.com/photo-1555507036-ab1f4038808a?auto=format&fit=crop&w=900&q=82",true},
  {"Pastries","Cinnamon Roll",105,0,410,"https://loremflickr.com/900/700/cinnamon,roll,pastry?lock=1010",true},
  {"Desserts","Chocolate Cake",125,12,390,"https://images.unsplash.com/photo-1578985545062-69928b1d9587?auto=format&fit=crop&w=900&q=82",true},
  {"Desserts","Cheesecake",135,0,350,"https://loremflickr.com/900/700/cheesecake,dessert?lock=1212",true}};
  for(Object[] r:rows){String name=(String)r[1];if(!products.existsByName(name))products.save(new Product(cats.findByName((String)r[0]),name,(int)r[2],(int)r[3],(int)r[4],(String)r[5],(boolean)r[6]));}
 };}
}
