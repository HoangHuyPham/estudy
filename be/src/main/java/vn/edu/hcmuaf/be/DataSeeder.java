package vn.edu.hcmuaf.be;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import vn.edu.hcmuaf.be.entity.Cart;
import vn.edu.hcmuaf.be.entity.Course;
import vn.edu.hcmuaf.be.entity.Customer;
import vn.edu.hcmuaf.be.entity.Image;
import vn.edu.hcmuaf.be.entity.Instructor;
import vn.edu.hcmuaf.be.entity.Lecture;
import vn.edu.hcmuaf.be.entity.Role;
import vn.edu.hcmuaf.be.entity.Section;
import vn.edu.hcmuaf.be.entity.User;
import vn.edu.hcmuaf.be.entity.Video;
import vn.edu.hcmuaf.be.repository.UserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

        @Autowired
        private UserRepository userRepository;

        @Override
        public void run(String... args) {
                System.out.println("Data seeder phase");

                if (userRepository.count() == 0) {
                        Role roleAdmin = Role.builder().name("admin").build();
                        Role roleCustomer = Role.builder().name("customer").build();
                        Role roleInstructor = Role.builder().name("instructor").build();

                        Cart cart1 = Cart.builder().build();
                        Cart cart2 = Cart.builder().build();
                        Cart cart3 = Cart.builder().build();

                        User userAdmin = User.builder().username("admin")
                                        .password(BCrypt.hashpw("123", BCrypt.gensalt())).build();
                        User userCustomer = User.builder().username("customer")
                                        .password(BCrypt.hashpw("123", BCrypt.gensalt()))
                                        .build();
                        User userInstructor = User.builder().username("instructor")
                                        .password(BCrypt.hashpw("123", BCrypt.gensalt()))
                                        .build();

                        Customer customer1 = Customer.builder().displayName("A Nguyen").build();
                        Instructor instructor1 = Instructor.builder().displayName("Thormas Edison")
                                        .description("Pro teacher <3").build();

                        String previewUrl = "http://localhost:8080/public/df793c44-c9df-4639-9013-56515798120b.png";
                        String videoUrl = "http://localhost:8080/public/0d6c33f3-b506-48fa-8a81-6fa11e752efd.mp4";

                        for (int i = 1; i <= 20; i++) {

                                Video v1 = Video.builder().duration(2302).name("no name").url(videoUrl).build();
                                Video v2 = Video.builder().duration(2302).name("no name").url(videoUrl).build();
                                Video v3 = Video.builder().duration(2302).name("no name").url(videoUrl).build();
                                Video v4 = Video.builder().duration(2302).name("no name").url(videoUrl).build();
                                
                                Image img = Image.builder().name("no name").url(previewUrl).build();

                                Lecture l1 = Lecture.builder().title("Hello world " + i).ordinal(1)
                                                .description("Giới thiệu cơ bản " + i).build();
                                l1.addVideo(v1);
                                Lecture l2 = Lecture.builder().title("Trước khi bắt đầu " + i).ordinal(2)
                                                .description("Cần chuẩn bị gì " + i).build();
                                l2.addVideo(v2);
                                Lecture l3 = Lecture.builder().title("Cài đặt môi trường " + i).ordinal(1)
                                                .description("IDE, Compiler " + i).build();
                                l3.addVideo(v3);
                                Lecture l4 = Lecture.builder().title("Biến và kiểu dữ liệu " + i).ordinal(2)
                                                .description("Phần cơ bản nhất " + i).build();
                                l4.addVideo(v4);

                                Section s1 = Section.builder().name("Mở đầu " + i).ordinal(1).build();
                                s1.addLecture(l1);
                                s1.addLecture(l2);

                                Section s2 = Section.builder().name("Cài đặt và Biến " + i).ordinal(2).build();
                                s2.addLecture(l3);
                                s2.addLecture(l4);

                                Course course = Course.builder()
                                                .name("Lập trình C cơ bản " + i)
                                                .oldPrice(1100000 + (i * 10000))
                                                .currentPrice(900000 + (i * 8000))
                                                .description("Khóa học số " + i
                                                                + ", bao gồm nhiều nội dung hấp dẫn và thực tế.")
                                                .build();

                                course.addPreview(img);
                                course.addSection(s1);
                                course.addSection(s2);
                                instructor1.addCourse(course);
                        }

                        userAdmin.addRole(roleAdmin);
                        userAdmin.addCart(cart1);
                        userRepository.save(userAdmin);

                        userCustomer.addRole(roleCustomer);
                        userCustomer.addCustomer(customer1);
                        userCustomer.addCart(cart2);
                        userRepository.save(userCustomer);

                        userInstructor.addRole(roleInstructor);
                        userInstructor.addInstructor(instructor1);
                        userInstructor.addCart(cart3);
                        userRepository.save(userInstructor);

                }
        }
}