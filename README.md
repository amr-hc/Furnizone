# E-commerce (Laravel - Angular)

<!-- sql code -->

### Database connection

#### First open mysql

create this user using the root DB user to start working on the database, it's heavily advised to change the credentials:

```sql
CREATE USER 'e-commerce'@'%' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON *.* TO 'e-commerce'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;
exit
```

#### Then open the terminal

enter your password

```sql

mysql -u e-commerce -p

CREATE DATABASE e_commerce;
-- insure the database works
USE e_commerce;
```

#### Install the necesasry dependencies

```bash
composer install
# php artisan migrate
# php artisan db:seed
php artisan jwt:secret
php artisan serve
```
