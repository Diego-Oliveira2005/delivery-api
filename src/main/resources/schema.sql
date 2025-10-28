CREATE TABLE customers (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          email VARCHAR(100) NOT NULL,
                          phone VARCHAR(20),
                          address VARCHAR(200),
                          date_register TIMESTAMP,
                          active BOOLEAN
);

CREATE TABLE restaurants (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              name VARCHAR(100) NOT NULL,
                              category VARCHAR(50),
                              address VARCHAR(200),
                              phone VARCHAR(20),
                              delivery_fee DECIMAL(10,2),
                              rating DECIMAL(2,1),
                              active BOOLEAN
);

CREATE TABLE products (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          description VARCHAR(200),
                          price DECIMAL(10,2),
                          category VARCHAR(50),
                          available BOOLEAN,
                          restaurant_id INT
);

CREATE TABLE orders (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        order_number VARCHAR(20) NOT NULL,
                        order_date TIMESTAMP,
                        status VARCHAR(20),
                        total_value DECIMAL(10, 2),
                        misc VARCHAR(200),
                        customer_id INT,
                        restaurant_id INT,
                        items VARCHAR(200),
                        FOREIGN KEY (customer_id) REFERENCES customers(id),
                        FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
);