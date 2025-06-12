import React from 'react';
import '../styles/Home.css';

export const Home: React.FC = () => {
    return (
        <div className="home-page">
            <header className="home-header">
                <h1>Chào mừng đến với Học Online</h1>
                <p>Khám phá hàng trăm khóa học chất lượng, từ lập trình đến kỹ năng mềm.</p>
            </header>

            <section className="featured-courses">
                <h2>Khóa học nổi bật</h2>
                <div className="course-list">
                    <div className="course-card">
                        <h3>Lập trình React</h3>
                        <p>Học cách xây dựng ứng dụng web hiện đại với ReactJS.</p>
                        <button>Xem chi tiết</button>
                    </div>
                    <div className="course-card">
                        <h3>Python cho người mới bắt đầu</h3>
                        <p>Khóa học nền tảng giúp bạn làm quen với Python.</p>
                        <button>Xem chi tiết</button>
                    </div>
                </div>
            </section>

            <section className="why-choose-us">
                <h2>Tại sao chọn chúng tôi?</h2>
                <ul>
                    <li>Giảng viên chất lượng và giàu kinh nghiệm</li>
                    <li>Nội dung khóa học được cập nhật liên tục</li>
                    <li>Học mọi lúc mọi nơi, trên mọi thiết bị</li>
                </ul>
            </section>
        </div>
    );
};
