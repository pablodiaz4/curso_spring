INSERT INTO customer (name, email)
              VALUES('CLIENTE1', 'CLIENTE1@EMAIL.COM');
INSERT INTO customer (name, email)
              VALUES('COMPANY1', 'COMPANY1@EMAIL.COM');

INSERT INTO accounts (type, opening_Date, balance, owner_Id)
              VALUES('PERSONAL', NOW(), 2500.00,(SELECT id  FROM customer where name = 'CLIENTE1'));
INSERT INTO accounts (type, opening_Date, balance, owner_Id)
              VALUES('PERSONAL', NOW(), 1500.00,(SELECT id  FROM customer where name = 'CLIENTE1'));
INSERT INTO accounts (type, opening_Date, balance, owner_Id)
              VALUES('PERSONAL', NOW(), 1000.00,(SELECT id  FROM customer where name = 'CLIENTE1'));
