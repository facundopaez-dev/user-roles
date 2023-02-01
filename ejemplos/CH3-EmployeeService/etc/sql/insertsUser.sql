-- IRRIGATION_SYSTEM_USER TABLE
INSERT INTO IRRIGATION_SYSTEM_USER (USERNAME, PASSWORD, EMAIL, ACTIVE, SUPERUSER) VALUES ('admin', 'admin', 'admin@eservice.com', 1, 1);
INSERT INTO IRRIGATION_SYSTEM_USER (USERNAME, PASSWORD, EMAIL, ACTIVE, SUPERUSER) VALUES ('jane', 'jane', 'jane@eservice.com', 1, 1);
INSERT INTO IRRIGATION_SYSTEM_USER (USERNAME, PASSWORD, EMAIL, ACTIVE, SUPERUSER) VALUES ('john', 'john', 'john@eservice.com', 1, 0);
INSERT INTO IRRIGATION_SYSTEM_USER (USERNAME, PASSWORD, EMAIL, ACTIVE, SUPERUSER) VALUES ('anya', 'anya', 'anya@eservice.com', 1, 0);
INSERT INTO IRRIGATION_SYSTEM_USER (USERNAME, PASSWORD, EMAIL, ACTIVE, SUPERUSER) VALUES ('jimmy', 'jimmy', 'jimmy@eservice.com', 1, 0);

-- Este usuario es utilizado para la prueba unitaria dos del metodo isActive de la clase UserServiceBean. Si lee la descripcion de esta prueba unitaria
-- en la clase UserServiceBeanTest, se dara cuenta de que NO se debe establecer el atributo "active" en uno (1), ya que de hacerlo, dicha prueba fallara.
INSERT INTO IRRIGATION_SYSTEM_USER (USERNAME, PASSWORD, EMAIL, ACTIVE, SUPERUSER) VALUES ('taylor', 'taylor', 'taylor@eservice.com', 0, 0);