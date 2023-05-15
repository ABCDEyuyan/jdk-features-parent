create table userinfo
(
    id int auto_increment primary key,
    username char(10),
    pwd char(10)

);

create table category(
                         id int auto_increment primary key ,
                         cname char(10)
);

create table product(
                        id int auto_increment primary key ,
                        pname char(10),
                        price decimal(10,2),
                        image char(20),
                        qty int,
                        status bit,
                        pubdate date,
                        cid int  references category(id)
);
insert into userinfo(username, pwd) values('admin','123') ;

insert into category(cname) values('衣服');
insert into category(cname) values('电脑');
insert into category(cname) values('键盘');

insert into product(pname, price, image, qty, status, pubdate, cid)
values('成衣',100,'1.jpg',50,1,'2023-5-10',1),
      ('裤子',50,'2.jpg',50,1,'2023-5-8',1),
      ('mac',12000,'1.jpg',30,1,'2023-4-10',2),
      ('huawei',10000,'2.jpg',150,1,'2022-5-10',2),
      ('绿联',299,'1.jpg',30,1,'2022-3-10',3);

select * from userinfo;
select * from category;
select * from product;
