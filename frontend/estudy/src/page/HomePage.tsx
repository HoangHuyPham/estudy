import { Button } from "@/component/base/button"
import {
    Card,
    CardDescription,
    CardHeader,
    CardTitle,
} from "@/component/base/card"
import { Input } from "@/component/base/input"

const topicTabs = ["Tất cả", "Mới nhất", "Phổ biến", "Đề xuất"]

const mockCourses = [
    {
        id: 1,
        title: "React - Cơ bản đến nâng cao",
        teacher: "ThS. Nguyễn Văn A",
        price: "279.000đ",
        oldPrice: "500.000đ",
        duration: "40 giờ",
        students: 120,
        image: "https://avatar.vercel.sh/react-course",
    },
    {
        id: 2,
        title: "TypeScript thực chiến cho Frontend",
        teacher: "ThS. Lê Minh K",
        price: "319.000đ",
        oldPrice: "590.000đ",
        duration: "36 giờ",
        students: 95,
        image: "https://avatar.vercel.sh/typescript-course",
    },
    {
        id: 3,
        title: "Java Spring Boot từ Zero",
        teacher: "ThS. Trần Quốc H",
        price: "399.000đ",
        oldPrice: "690.000đ",
        duration: "52 giờ",
        students: 168,
        image: "https://avatar.vercel.sh/spring-course",
    },
    {
        id: 4,
        title: "UI/UX cho Developer",
        teacher: "ThS. Phạm Thu M",
        price: "249.000đ",
        oldPrice: "450.000đ",
        duration: "24 giờ",
        students: 78,
        image: "https://avatar.vercel.sh/uiux-course",
    },
    {
        id: 5,
        title: "Node.js API Mastery",
        teacher: "ThS. Đặng Hoàng N",
        price: "349.000đ",
        oldPrice: "620.000đ",
        duration: "44 giờ",
        students: 133,
        image: "https://avatar.vercel.sh/node-course",
    },
    {
        id: 6,
        title: "SQL & Database Essentials",
        teacher: "ThS. Vũ Thanh T",
        price: "199.000đ",
        oldPrice: "390.000đ",
        duration: "22 giờ",
        students: 110,
        image: "https://avatar.vercel.sh/sql-course",
    },
]

export const HomePage: React.FC = () => {
    return (
        <div className="flex h-full w-full flex-col gap-4 py-4">
            <section className="rounded-lg bg-muted p-4 md:p-5">
                <div className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
                    <div className="space-y-1">
                        <h1 className="font-heading text-2xl font-bold">Học cùng eStudy</h1>
                        <p className="text-sm text-muted-foreground">Khám phá khoá học và tài nguyên phù hợp với bạn</p>
                    </div>

                    <div className="flex w-full max-w-md items-center gap-2">
                        <Input
                            type="text"
                            placeholder="Tìm khóa học, chủ đề..."
                        />
                        <Button size="lg">Tìm</Button>
                    </div>
                </div>
            </section>

            <section className="text-muted-foreground flex flex-wrap gap-2">
                {topicTabs.map((tab, index) => (
                    <Button key={tab} variant={index === 0 ? "default" : "outline"} size="sm">
                        {tab}
                    </Button>
                ))}
            </section>

            <section className="grid grid-cols-1 gap-4 md:grid-cols-2 xl:grid-cols-3">
                {mockCourses.map((course) => (
                    <Card key={course.id} className="cursor-pointer relative pt-0 overflow-hidden opacity-80 hover:opacity-100 transition-opacity">
                        <div className="absolute inset-x-0 top-0 z-30 aspect-video bg-black/35" />
                        <img
                            src={course.image}
                            alt={course.title}
                            className="relative z-20 aspect-video w-full object-cover brightness-60 grayscale dark:brightness-40"
                        />

                        <CardHeader>
                            <CardTitle className="line-clamp-2 text-xl font-bold">{course.title}</CardTitle>
                            <CardDescription>{course.teacher}</CardDescription>
                            <CardDescription className="text-xl font-bold">
                                <span>{course.price}</span>
                                <span className="ml-2 text-base font-normal text-muted-foreground line-through">{course.oldPrice}</span>
                            </CardDescription>
                            <CardDescription>Thời lượng: {course.duration}</CardDescription>
                            <CardDescription>Số người tham gia: {course.students}</CardDescription>
                        </CardHeader>
                    </Card>
                ))}
            </section>
        </div>
    )
}