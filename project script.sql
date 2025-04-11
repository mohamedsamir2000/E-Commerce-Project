
CREATE TABLE `test`.`cutomers` (
  `Customer_id` INT NOT NULL,
  `First_name` VARCHAR(45) NOT NULL,
  `Last_name` VARCHAR(45) NOT NULL,
  `Age` INT NOT NULL,
  `Country` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Customer_id`));


INSERT INTO cutomers (Customer_id,First_name,Last_name, Age,Country)
VALUES 
    (1, 'John','Doe',31,'USA'),(2,'Robert','Luna', 22,'USA'),(3,'David','Robinson',22,'UK'),
    (4, 'John','Reinhardt', 25,'UK'),(5, 'Betty','Doe', 28,'UAE');
	
	
	
	
	SELECT `cutomers`.`Customer_id`,
    `cutomers`.`First_name`,
    `cutomers`.`Last_name`,
    `cutomers`.`Age`,
    `cutomers`.`Country`
FROM `test`.`cutomers`;



CREATE TABLE `test`.`orders` (
  `Order_id` INT NOT NULL,
  `Item` VARCHAR(45) NOT NULL,
  `Amount` INT NOT NULL,
  `Customer_id` INT NOT NULL,
  PRIMARY KEY (`Order_id`),
  INDEX `fk_customer_idx` (`Customer_id` ASC) VISIBLE,
  CONSTRAINT `fk_customer`
    FOREIGN KEY (`Customer_id`)
    REFERENCES `test`.`cutomers` (`Customer_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


INSERT INTO orders (Order_id, Item,Amount,Customer_id)
VALUES 
    (1, 'Keyboard', 400,4),(2, 'Mouse', 300,4),(3, 'Monitor', 12000,3),(4, 'Keyboard', 400,1),
    (5, 'Mousepad', 250,2);
	
	CREATE TABLE `test`.`shippings` (
  `shipping_id` INT NOT NULL,
  `status` VARCHAR(10) NOT NULL,
  `customer` INT NOT NULL,
  PRIMARY KEY (`shipping_id`));
  
  
  INSERT INTO Shippings (shipping_id, status,customer)
VALUES 
    (1, 'Pending', 2),(2, 'Pending', 4),(3, 'Delivered', 3),(4, 'Pending', 5),
    (5, 'Delivered', 1);



ALTER TABLE customers
ADD City VARCHAR(50);
ALTER TABLE customers
ADD Phone_number INT;

INSERT INTO customers
VALUES (6,'Mohamed','Ramzy',24,'EGY','GIZA');

SELECT * FROM customers 
WHERE customer_id=6;

ALTER TABLE customers
DROP column Phone_number; 

SELECT item, amount*0.1 AS DICOUNT
FROM orders
ORDER BY amount;


CREATE TABLE `test`.`products` (
  `product_id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `quantity_in_stock` INT NOT NULL,
  `unite_price` FLOAT NOT NULL,
  PRIMARY KEY (`product_id`));


CREATE TABLE `test`.`order_items` (
  `order_items_id` INT NOT NULL AUTO_INCREMENT,
  `product_id` INT NOT NULL,
  `quantity` INT NOT NULL,
  `unit_price` FLOAT NOT NULL,
  PRIMARY KEY (`order_items_id`));



ALTER TABLE `test`.`order_items` 
ADD INDEX `product_fk_idx` (`product_id` ASC) VISIBLE;
;
ALTER TABLE `test`.`order_items` 
ADD CONSTRAINT `product_fk`
  FOREIGN KEY (`product_id`)
  REFERENCES `test`.`products` (`product_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  
  INSERT INTO `test`.`products`(`product_id`,`name`,`quantity_in_stock`,`unite_price`)
VALUES(1,"foam dinner plate",70,1.21),(2,"pork bacon,back peameal",49,4.65),(3,"lettuce_romaine,Heart",38,3.35),(4,"brocolinni_gayian,chinese",90,4.53),
(5,"sauce_ranchdressing",94,1.63),(6,"pebit baguette",14,2.39),(7,"sweet pea sprouts",98,3.29),(8,"island oasis_raspberry",26,0.74),
(9,"longan",67,2.26),(10,"brom_push",6,1.09);

INSERT INTO `test`.`order_items`(`order_items_id`,`product_id`,`quantity`,`unit_price`)
VALUES(1,4,4,3.74), (2,1,2,9.10), (3,4,4,1.66), (4,6,2,2.944), (5,3,10,9.12), (6,3,7,6.99);

select o.order_items_id,p.name,p.quantity_in_stock
from order_items o inner join products p
on o.product_id=p.product_id
order by o.order_items_id;

select o.order_items_id, p.name, p.quantity_in_stock
from order_items o join products p
using (product_id)
order by o.order_items_id;

select c.First_name, c.Age, o.Item, o.Amount*0.1 as discount
from customers c left join orders o
on c.Customer_id = o.Customer_id
order by o.Amount;