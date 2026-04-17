package org.example.project3.Model;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * This component runs automatically when the Spring Boot application starts.
 * It seeds the 'ngoregistration', 'donor_registration', and 'food_donar' tables if they are empty.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DataLoader(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        seedNgoData();
        seedDonorData();
        seedFoodDonarData();
    }

    private void seedNgoData() {
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM ngoregistration", Integer.class);

            if (count != null && count == 0) {
                System.out.println("Seeding 50 NGO records into jkuserdb...");

                String sql = "INSERT INTO ngoregistration " +
                        "(ngo_name, registration_number, ngo_type, email, password, contact_person, mobile, capacity, address, city, pincode, latitude, longitude, altitude, file_name, filepath) " +
                        "VALUES " +
                        "('NGO1','REG1001','Food','ngo1@mail.com','123456','Person1','9000000001',50,'Area1','Pune','411001',18.5204,73.8567,560,'f1.pdf','/uploads/f1.pdf'), " +
                        "('NGO2','REG1002','Food','ngo2@mail.com','123456','Person2','9000000002',60,'Area2','Mumbai','400001',19.0760,72.8777,14,'f2.pdf','/uploads/f2.pdf'), " +
                        "('NGO3','REG1003','Food','ngo3@mail.com','123456','Person3','9000000003',70,'Area3','Nagpur','440001',21.1458,79.0882,310,'f3.pdf','/uploads/f3.pdf'), " +
                        "('NGO4','REG1004','Food','ngo4@mail.com','123456','Person4','9000000004',80,'Area4','Delhi','110001',28.7041,77.1025,216,'f4.pdf','/uploads/f4.pdf'), " +
                        "('NGO5','REG1005','Food','ngo5@mail.com','123456','Person5','9000000005',90,'Area5','Bangalore','560001',12.9716,77.5946,920,'f5.pdf','/uploads/f5.pdf'), " +
                        "('NGO6','REG1006','Food','ngo6@mail.com','123456','Person6','9000000006',100,'Area6','Hyderabad','500001',17.3850,78.4867,542,'f6.pdf','/uploads/f6.pdf'), " +
                        "('NGO7','REG1007','Food','ngo7@mail.com','123456','Person7','9000000007',110,'Area7','Chennai','600001',13.0827,80.2707,6,'f7.pdf','/uploads/f7.pdf'), " +
                        "('NGO8','REG1008','Food','ngo8@mail.com','123456','Person8','9000000008',120,'Area8','Kolkata','700001',22.5726,88.3639,9,'f8.pdf','/uploads/f8.pdf'), " +
                        "('NGO9','REG1009','Food','ngo9@mail.com','123456','Person9','9000000009',130,'Area9','Ahmedabad','380001',23.0225,72.5714,53,'f9.pdf','/uploads/f9.pdf'), " +
                        "('NGO10','REG1010','Food','ngo10@mail.com','123456','Person10','9000000010',140,'Area10','Jaipur','302001',26.9124,75.7873,431,'f10.pdf','/uploads/f10.pdf'), " +
                        "('NGO11','REG1011','Food','ngo11@mail.com','123456','Person11','9000000011',55,'Area11','Surat','395001',21.1702,72.8311,13,'f11.pdf','/uploads/f11.pdf'), " +
                        "('NGO12','REG1012','Food','ngo12@mail.com','123456','Person12','9000000012',65,'Area12','Lucknow','226001',26.8467,80.9462,123,'f12.pdf','/uploads/f12.pdf'), " +
                        "('NGO13','REG1013','Food','ngo13@mail.com','123456','Person13','9000000013',75,'Area13','Kanpur','208001',26.4499,80.3319,126,'f13.pdf','/uploads/f13.pdf'), " +
                        "('NGO14','REG1014','Food','ngo14@mail.com','123456','Person14','9000000014',85,'Area14','Indore','452001',22.7196,75.8577,553,'f14.pdf','/uploads/f14.pdf'), " +
                        "('NGO15','REG1015','Food','ngo15@mail.com','123456','Person15','9000000015',95,'Area15','Bhopal','462001',23.2599,77.4126,527,'f15.pdf','/uploads/f15.pdf'), " +
                        "('NGO16','REG1016','Food','ngo16@mail.com','123456','Person16','9000000016',105,'Area16','Patna','800001',25.5941,85.1376,53,'f16.pdf','/uploads/f16.pdf'), " +
                        "('NGO17','REG1017','Food','ngo17@mail.com','123456','Person17','9000000017',115,'Area17','Ranchi','834001',23.3441,85.3096,651,'f17.pdf','/uploads/f17.pdf'), " +
                        "('NGO18','REG1018','Food','ngo18@mail.com','123456','Person18','9000000018',125,'Area18','Chandigarh','160001',30.7333,76.7794,365,'f18.pdf','/uploads/f18.pdf'), " +
                        "('NGO19','REG1019','Food','ngo19@mail.com','123456','Person19','9000000019',135,'Area19','Dehradun','248001',30.3165,78.0322,640,'f19.pdf','/uploads/f19.pdf'), " +
                        "('NGO20','REG1020','Food','ngo20@mail.com','123456','Person20','9000000020',145,'Area20','Shimla','171001',31.1048,77.1734,2276,'f20.pdf','/uploads/f20.pdf'), " +
                        "('NGO21','REG1021','Food','ngo21@mail.com','123456','Person21','9000000021',50,'Area21','Goa','403001',15.2993,74.1240,5,'f21.pdf','/uploads/f21.pdf'), " +
                        "('NGO22','REG1022','Food','ngo22@mail.com','123456','Person22','9000000022',60,'Area22','Mysore','570001',12.2958,76.6394,763,'f22.pdf','/uploads/f22.pdf'), " +
                        "('NGO23','REG1023','Food','ngo23@mail.com','123456','Person23','9000000023',70,'Area23','Coimbatore','641001',11.0168,76.9558,411,'f23.pdf','/uploads/f23.pdf'), " +
                        "('NGO24','REG1024','Food','ngo24@mail.com','123456','Person24','9000000024',80,'Area24','Madurai','625001',9.9252,78.1198,101,'f24.pdf','/uploads/f24.pdf'), " +
                        "('NGO25','REG1025','Food','ngo25@mail.com','123456','Person25','9000000025',90,'Area25','Trichy','620001',10.7905,78.7047,88,'f25.pdf','/uploads/f25.pdf'), " +
                        "('NGO26','REG1026','Food','ngo26@mail.com','123456','Person26','9000000026',100,'Area26','Visakhapatnam','530001',17.6868,83.2185,45,'f26.pdf','/uploads/f26.pdf'), " +
                        "('NGO27','REG1027','Food','ngo27@mail.com','123456','Person27','9000000027',110,'Area27','Vijayawada','520001',16.5062,80.6480,39,'f27.pdf','/uploads/f27.pdf'), " +
                        "('NGO28','REG1028','Food','ngo28@mail.com','123456','Person28','9000000028',120,'Area28','Guntur','522001',16.3067,80.4365,33,'f28.pdf','/uploads/f28.pdf'), " +
                        "('NGO29','REG1029','Food','ngo29@mail.com','123456','Person29','9000000029',130,'Area29','Warangal','506001',17.9689,79.5941,302,'f29.pdf','/uploads/f29.pdf'), " +
                        "('NGO30','REG1030','Food','ngo30@mail.com','123456','Person30','9000000030',140,'Area30','Nashik','422001',19.9975,73.7898,560,'f30.pdf','/uploads/f30.pdf'), " +
                        "('NGO31','REG1031','Food','ngo31@mail.com','123456','Person31','9000000031',55,'Area31','Kolhapur','416001',16.7050,74.2433,569,'f31.pdf','/uploads/f31.pdf'), " +
                        "('NGO32','REG1032','Food','ngo32@mail.com','123456','Person32','9000000032',65,'Area32','Solapur','413001',17.6599,75.9064,457,'f32.pdf','/uploads/f32.pdf'), " +
                        "('NGO33','REG1033','Food','ngo33@mail.com','123456','Person33','9000000033',75,'Area33','Satara','415001',17.6805,74.0183,742,'f33.pdf','/uploads/f33.pdf'), " +
                        "('NGO34','REG1034','Food','ngo34@mail.com','123456','Person34','9000000034',85,'Area34','Amravati','444601',20.9374,77.7796,343,'f34.pdf','/uploads/f34.pdf'), " +
                        "('NGO35','REG1035','Food','ngo35@mail.com','123456','Person35','9000000035',95,'Area35','Akola','444001',20.7002,77.0082,282,'f35.pdf','/uploads/f35.pdf'), " +
                        "('NGO36','REG1036','Food','ngo36@mail.com','123456','Person36','9000000036',105,'Area36','Jalgaon','425001',21.0077,75.5626,209,'f36.pdf','/uploads/f36.pdf'), " +
                        "('NGO37','REG1037','Food','ngo37@mail.com','123456','Person37','9000000037',115,'Area37','Latur','413512',18.4088,76.5604,631,'f37.pdf','/uploads/f37.pdf'), " +
                        "('NGO38','REG1038','Food','ngo38@mail.com','123456','Person38','9000000038',125,'Area38','Nanded','431601',19.1383,77.3210,362,'f38.pdf','/uploads/f38.pdf'), " +
                        "('NGO39','REG1039','Food','ngo39@mail.com','123456','Person39','9000000039',135,'Area39','Parbhani','431401',19.2608,76.7748,347,'f39.pdf','/uploads/f39.pdf'), " +
                        "('NGO40','REG1040','Food','ngo40@mail.com','123456','Person40','9000000040',145,'Area40','Beed','431122',18.9891,75.7601,530,'f40.pdf','/uploads/f40.pdf'), " +
                        "('NGO41','REG1041','Food','ngo41@mail.com','123456','Person41','9000000041',60,'Area41','Udaipur','313001',24.5854,73.7125,598,'f41.pdf','/uploads/f41.pdf'), " +
                        "('NGO42','REG1042','Food','ngo42@mail.com','123456','Person42','9000000042',70,'Area42','Jodhpur','342001',26.2389,73.0243,231,'f42.pdf','/uploads/f42.pdf'), " +
                        "('NGO43','REG1043','Food','ngo43@mail.com','123456','Person43','9000000043',80,'Area43','Ajmer','305001',26.4499,74.6399,486,'f43.pdf','/uploads/f43.pdf'), " +
                        "('NGO44','REG1044','Food','ngo44@mail.com','123456','Person44','9000000044',90,'Area44','Alwar','301001',27.5530,76.6346,268,'f44.pdf','/uploads/f44.pdf'), " +
                        "('NGO45','REG1045','Food','ngo45@mail.com','123456','Person45','9000000045',100,'Area45','Kota','324001',25.2138,75.8648,271,'f45.pdf','/uploads/f45.pdf'), " +
                        "('NGO46','REG1046','Food','ngo46@mail.com','123456','Person46','9000000046',110,'Area46','Gwalior','474001',26.2183,78.1828,197,'f46.pdf','/uploads/f46.pdf'), " +
                        "('NGO47','REG1047','Food','ngo47@mail.com','123456','Person47','9000000047',120,'Area47','Jabalpur','482001',23.1815,79.9864,411,'f47.pdf','/uploads/f47.pdf'), " +
                        "('NGO48','REG1048','Food','ngo48@mail.com','123456','Person48','9000000048',130,'Area48','Raipur','492001',21.2514,81.6296,298,'f48.pdf','/uploads/f48.pdf'), " +
                        "('NGO49','REG1049','Food','ngo49@mail.com','123456','Person49','9000000049',140,'Area49','Bilaspur','495001',22.0797,82.1409,264,'f49.pdf','/uploads/f49.pdf'), " +
                        "('NGO50','REG1050','Food','ngo50@mail.com','123456','Person50','9000000050',150,'Area50','Durg','491001',21.1904,81.2849,280,'f50.pdf','/uploads/f50.pdf');";

                jdbcTemplate.execute(sql);
                System.out.println(">>> 50 NGO records seeded into jkuserdb successfully.");
            } else {
                System.out.println("Data already exists in 'ngoregistration' table. Skipping seeding.");
            }
        } catch (Exception e) {
            System.err.println("Error seeding NGO data: " + e.getMessage());
        }
    }

    private void seedDonorData() {
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM donor_registration", Integer.class);

            if (count != null && count == 0) {
                System.out.println("Seeding donor records into the database...");

                String sql = "INSERT INTO donor_registration " +
                        "(full_name, email, mobile, password, address, city, state, pincode, donor_type, organization_name, food_type, quantity, pickup_time, fssai, file_name, filepath, latitude, longitude, altitude) " +
                        "VALUES " +
                        "('Rahul Patil', 'rahul1@gmail.com', '9876543210', 'pass123', 'Main Road, Shivaji Nagar', 'Pune', 'Maharashtra', '411001', 'Individual', NULL, 'Cooked Food', '10 kg', '10:00 AM', NULL, 'doc1.pdf', '/uploads/doc1.pdf', 18.5204, 73.8567, 560), " +
                        "('Sneha Deshmukh', 'sneha@gmail.com', '9876543211', 'pass123', 'MG Road', 'Mumbai', 'Maharashtra', '400001', 'Individual', NULL, 'Packed Food', '5 kg', '12:00 PM', NULL, 'doc2.pdf', '/uploads/doc2.pdf', 19.0760, 72.8777, 14), " +
                        "('Anil More', 'anil@gmail.com', '9876543212', 'pass123', 'Station Road', 'Nashik', 'Maharashtra', '422001', 'NGO', 'Helping Hands NGO', 'Raw Food', '25 kg', '09:00 AM', 'FSSAI12345', 'doc3.pdf', '/uploads/doc3.pdf', 19.9975, 73.7898, 565), " +
                        "('Priya Jadhav', 'priya@gmail.com', '9876543213', 'pass123', 'Market Yard', 'Kolhapur', 'Maharashtra', '416001', 'Individual', NULL, 'Cooked Food', '8 kg', '01:00 PM', NULL, 'doc4.pdf', '/uploads/doc4.pdf', 16.7050, 74.2433, 545), " +
                        "('Vikram Shinde', 'vikram@gmail.com', '9876543214', 'pass123', 'Bus Stand Area', 'Satara', 'Maharashtra', '415001', 'Restaurant', 'Shinde Foods', 'Cooked Food', '50 kg', '11:30 AM', 'FSSAI54321', 'doc5.pdf', '/uploads/doc5.pdf', 17.6805, 74.0183, 712), " +
                        "('Kiran Pawar', 'kiran@gmail.com', '9876543215', 'pass123', 'Railway Colony', 'Solapur', 'Maharashtra', '413001', 'Individual', NULL, 'Packed Food', '6 kg', '02:00 PM', NULL, 'doc6.pdf', '/uploads/doc6.pdf', 17.6599, 75.9064, 483), " +
                        "('Rohit Mane', 'rohit@gmail.com', '9876543216', 'pass123', 'College Road', 'Nanded', 'Maharashtra', '431601', 'NGO', 'Food For All', 'Raw Food', '30 kg', '09:30 AM', 'FSSAI67890', 'doc7.pdf', '/uploads/doc7.pdf', 19.1383, 77.3210, 362), " +
                        "('Pooja Kadam', 'pooja@gmail.com', '9876543217', 'pass123', 'Civil Line', 'Amravati', 'Maharashtra', '444601', 'Individual', NULL, 'Cooked Food', '12 kg', '03:00 PM', NULL, 'doc8.pdf', '/uploads/doc8.pdf', 20.9374, 77.7796, 343), " +
                        "('Sagar Nikam', 'sagar@gmail.com', '9876543218', 'pass123', 'Temple Road', 'Nagpur', 'Maharashtra', '440001', 'Restaurant', 'Spice Hub', 'Cooked Food', '60 kg', '10:30 AM', 'FSSAI11111', 'doc9.pdf', '/uploads/doc9.pdf', 21.1458, 79.0882, 310), " +
                        "('Neha Kulkarni', 'neha@gmail.com', '9876543219', 'pass123', 'City Center', 'Aurangabad', 'Maharashtra', '431001', 'Individual', NULL, 'Packed Food', '7 kg', '04:00 PM', NULL, 'doc10.pdf', '/uploads/doc10.pdf', 19.8762, 75.3433, 580);";

                jdbcTemplate.execute(sql);
                System.out.println(">>> Donor records seeded successfully.");
            } else {
                System.out.println("Table 'donor_registration' already contains data. Skipping seeding.");
            }
        } catch (Exception e) {
            System.err.println("Error seeding donor data: " + e.getMessage());
        }
    }

    private void seedFoodDonarData() {
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM food_donar", Integer.class);

            if (count != null && count == 0) {
                System.out.println("Seeding food donar records into the database...");

                String sql = "INSERT INTO food_donar " +
                        "(food_name, food_type, meal_type, category, quantity, prepared_time, expiry_time, address, city, pickup_date, pickup_time, contact_name, mobile, instructions, statusrequest, file_name, filepath, restaurant_id, assigned_ngo_id) " +
                        "VALUES " +

                        "('Veg Thali', 'Cooked', 'Lunch', 'Veg', 25, '11:00 AM', 6, 'Shivaji Nagar', 'Pune', '2026-04-14', '12:30 PM', 'Rahul Patil', '9876500001', 'Handle carefully', 'Pending', 'Veg Thali.jpg', '/files/Veg Thali.jpg', 2, NULL), " +

                        "('Paneer Curry', 'Cooked', 'Dinner', 'Veg', 30, '08:00 PM', 8, 'MG Road', 'Mumbai', '2026-04-14', '09:00 PM', 'Sneha Deshmukh', '9876500002', 'Spicy food', 'Pending', 'PaneerCurry.jpg', '/files/Paneer Curry.jpg', 3, NULL), " +

                        "('Chicken Biryani', 'Cooked', 'Dinner', 'Non-Veg', 40, '07:30 PM', 5, 'Station Road', 'Nashik', '2026-04-14', '08:30 PM', 'Vikram Shinde', '9876500003', 'Hot food', 'Pending', 'Chicken Biryani.jpg', '/files/Chicken Biryani.jpg', 4, NULL), " +

                        "('Veg Rice', 'Cooked', 'Lunch', 'Veg', 20, '10:45 AM', 6, 'Market Yard', 'Kolhapur', '2026-04-14', '11:45 AM', 'Priya Jadhav', '9876500004', 'Fresh food', 'Pending', 'Veg Rice.jpg', '/files/Veg Rice.jpg', 5, NULL), " +

                        "('Fried Rice', 'Cooked', 'Dinner', 'Veg', 35, '06:30 PM', 7, 'Bus Stand Area', 'Satara', '2026-04-14', '07:30 PM', 'Anil More', '9876500005', 'Pack properly', 'Pending', 'Fried Rice.jpg', '/files/Fried Rice.jpg', 6, NULL), " +

                        "('Chapati Curry', 'Cooked', 'Lunch', 'Veg', 50, '12:00 PM', 6, 'Railway Colony', 'Solapur', '2026-04-14', '01:00 PM', 'Kiran Pawar', '9876500006', 'Urgent pickup', 'Pending', 'Chapati Curry.jpg', '/files/Chapati Curry.jpg', 7, NULL), " +

                        "('Veg Pulav', 'Cooked', 'Lunch', 'Veg', 28, '11:15 AM', 6, 'College Road', 'Nanded', '2026-04-14', '12:15 PM', 'Rohit Mane', '9876500007', 'Fresh only', 'Pending', 'Veg Pulav.jpg', '/files/Veg Pulav.jpg', 8, NULL), " +

                        "('Masala Dosa', 'Cooked', 'Breakfast', 'Veg', 22, '08:00 AM', 4, 'Civil Line', 'Amravati', '2026-04-14', '09:00 AM', 'Pooja Kadam', '9876500008', 'Crispy required', 'Pending', 'Masala Dosa.jpg', '/files/Masala Dosa.jpg', 9, NULL), " +

                        "('Egg Curry', 'Cooked', 'Dinner', 'Non-Veg', 32, '07:00 PM', 5, 'Temple Road', 'Nagpur', '2026-04-14', '08:00 PM', 'Sagar Nikam', '9876500009', 'Spicy level medium', 'Pending', 'Egg Curry.jpg', '/files/Egg Curry.jpg', 10, NULL), " +

                        "('Veg Sandwich', 'Packed', 'Breakfast', 'Veg', 18, '09:00 AM', 5, 'City Center', 'Aurangabad', '2026-04-14', '10:00 AM', 'Neha Kulkarni', '9876500010', 'Pack in box', 'Pending', 'Veg Sandwich.jpg', '/files/Veg Sandwich.jpg', 11, NULL), " +

                        "('Dal Rice', 'Cooked', 'Lunch', 'Veg', 45, '12:10 PM', 6, 'Shivaji Nagar', 'Pune', '2026-04-14', '01:00 PM', 'Rahul Patil', '9876500011', 'Distribute fast', 'Pending', 'Dal Rice.jpg', '/files/Dal Rice.jpg', 2, NULL), " +

                        "('Veg Biryani', 'Cooked', 'Dinner', 'Veg', 38, '08:10 PM', 7, 'MG Road', 'Mumbai', '2026-04-14', '09:10 PM', 'Sneha Deshmukh', '9876500012', 'Hot delivery', 'Pending', 'Veg Biryani.jpg', '/files/Veg Biryani.jpg', 3, NULL), " +

                        "('Chicken Curry', 'Cooked', 'Dinner', 'Non-Veg', 42, '07:45 PM', 5, 'Station Road', 'Nashik', '2026-04-14', '08:45 PM', 'Vikram Shinde', '9876500013', 'Handle carefully', 'Pending', 'Chicken Curry.jpg', '/files/Chicken Curry.jpg', 4, NULL), " +

                        "('Poha', 'Cooked', 'Breakfast', 'Veg', 26, '08:30 AM', 4, 'Market Yard', 'Kolhapur', '2026-04-14', '09:30 AM', 'Priya Jadhav', '9876500014', 'Fresh breakfast', 'Pending', 'Poha.jpg', '/files/Poha.jpg', 5, NULL), " +

                        "('Upma', 'Cooked', 'Breakfast', 'Veg', 20, '07:50 AM', 4, 'Bus Stand Area', 'Satara', '2026-04-14', '08:50 AM', 'Anil More', '9876500015', 'Soft texture', 'Pending', 'Upma.jpg', '/files/Upma.jpg', 6, NULL), " +

                        "('Veg Curry', 'Cooked', 'Lunch', 'Veg', 34, '12:20 PM', 6, 'Railway Colony', 'Solapur', '2026-04-14', '01:20 PM', 'Kiran Pawar', '9876500016', 'No spice overload', 'Pending', 'Veg Curry.jpg', '/files/Veg Curry.jpg', 7, NULL), " +

                        "('Rice Plate', 'Cooked', 'Lunch', 'Veg', 55, '11:40 AM', 6, 'College Road', 'Nanded', '2026-04-14', '12:40 PM', 'Rohit Mane', '9876500017', 'Urgent distribution', 'Pending', 'Rice Plate.jpg', '/files/Rice Plate.jpg', 8, NULL), " +

                        "('Idli Sambar', 'Cooked', 'Breakfast', 'Veg', 24, '08:15 AM', 4, 'Civil Line', 'Amravati', '2026-04-14', '09:15 AM', 'Pooja Kadam', '9876500018', 'Soft idli', 'Pending', 'Idli Sambar.jpg', '/files/Idli Sambar.jpg', 9, NULL), " +

                        "('Fish Curry', 'Cooked', 'Dinner', 'Non-Veg', 30, '07:20 PM', 5, 'Temple Road', 'Nagpur', '2026-04-14', '08:20 PM', 'Sagar Nikam', '9876500019', 'Fresh fish', 'Pending', 'Fish Curry.jpg', '/files/Fish Curry.jpg', 10, NULL), " +

                        "('Bread Jam Pack', 'Packed', 'Breakfast', 'Veg', 15, '09:10 AM', 5, 'City Center', 'Aurangabad', '2026-04-14', '10:10 AM', 'Neha Kulkarni', '9876500020', 'Pack properly', 'Pending', 'Bread Jam Pack.jpg', '/files/Bread Jam Pack.jpg', 11, NULL);";

                jdbcTemplate.execute(sql);
                System.out.println(">>> 20 Food donar records seeded successfully.");
            } else {
                System.out.println("Table 'food_donar' already contains data. Skipping seeding.");
            }
        } catch (Exception e) {
            System.err.println("Error seeding food donar data: " + e.getMessage());
        }
    }
}