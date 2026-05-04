import { Skeleton } from "@/component/base/skeleton";
import { Table, TableBody, TableRow, TableCell, TableHead, TableHeader } from "@/component/base/table";
import { Endpoints } from "@/constant";
import type { ICourse } from "@/interface";
import { axiosClient } from "@/lib/utils";
import { useEffect, useState } from "react";
import { AspectRatio } from '@/component/base/aspect-ratio';
import { Pagination, PaginationContent, PaginationEllipsis, PaginationItem, PaginationLink, PaginationNext, PaginationPrevious } from "@/component/base/pagination";
import { useNavigate } from "react-router-dom";

export const CoursesPage: React.FC = () => {

    const [courses, setCourses] = useState<ICourse[]>([]);
    const [loading, setLoading] = useState(false);
    const [pagination, setPagination] = useState({
        limit: 10,
        page: 0,
        totalItems: 0,
        totalPages: 0
    });
    const navigate = useNavigate();

    const handlePageChange = (targetPage: number) => {
        if (targetPage < 0 || targetPage >= pagination.totalPages || targetPage === pagination.page) {
            return;
        }

        setPagination((prev) => ({
            ...prev,
            page: targetPage,
        }));
    };

    const buildVisiblePages = () => {
        const totalPages = pagination.totalPages;
        const currentPage = pagination.page;

        if (totalPages <= 7) {
            return Array.from({ length: totalPages }, (_, index) => index);
        }

        if (currentPage <= 2) {
            return [0, 1, 2, 3, "ellipsis", totalPages - 1] as const;
        }

        if (currentPage >= totalPages - 3) {
            return [0, "ellipsis", totalPages - 4, totalPages - 3, totalPages - 2, totalPages - 1] as const;
        }

        return [0, "ellipsis", currentPage - 1, currentPage, currentPage + 1, "ellipsis", totalPages - 1] as const;
    };

    useEffect(() => {
        const abortController = new AbortController();

        const fetchCourses = async () => {
            setLoading(true);
            try {
                const response = await axiosClient.get(Endpoints.TUTOR.COURSES, {
                    signal: abortController.signal,
                    params: {
                        page: pagination.page,
                        limit: pagination.limit,
                    },
                });
                const { limit, page, totalItems, totalPages, data } = response.data;
                setCourses(data);
                setPagination({
                    limit,
                    page,
                    totalItems,
                    totalPages,
                });
            } catch (error) {
                if (abortController.signal.aborted) {
                    console.warn("Fetch courses aborted");
                } else {
                    console.error("Failed to fetch courses", error);
                }
            } finally {
                setLoading(false);
            }
        };

        fetchCourses();

        return () => {
            abortController.abort();
        };
    }, [pagination.page, pagination.limit]);


    const handleCourseEdit = (courseId: string) => { 
        navigate(`/tutor/manage-courses/course/${courseId}`)
    }

    return (
        <>
            {loading ? <Skeleton className="h-full w-full" /> : <>
                <Table>
                    <TableHeader>
                        <TableRow>
                            <TableHead className="text-center w-48">Tên</TableHead>
                            <TableHead className="text-center">Trạng thái</TableHead>
                            <TableHead className="text-center">Chế độ hiển thị</TableHead>
                            <TableHead className="text-center">Số học viên đăng kí</TableHead>
                            <TableHead className="text-center">Ngày tạo</TableHead>
                            <TableHead className="text-center">Ngày cập nhật</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {courses.length > 0 ? courses.map((course) => (
                            <TableRow className="cursor-pointer" key={course.id} onClick={() => handleCourseEdit(course.id)}>
                                <TableCell className="text-center">
                                    <div className="flex items-center gap-2">
                                        <div className="w-32">
                                            <AspectRatio ratio={16 / 9}>
                                                <img src={`${Endpoints.BACKEND}${Endpoints.SERVE}/${course.thumbnail}`} alt={course.name} className="rounded w-full h-full object-cover" />
                                            </AspectRatio>
                                        </div>
                                        {course.name}
                                    </div>
                                </TableCell>
                                <TableCell className="text-center">{course.status}</TableCell>
                                <TableCell className="text-center">{course.visibility}</TableCell>
                                <TableCell className="text-center">{course.enrollmentCount}</TableCell>
                                <TableCell className="text-center">{new Date(course.createdAt * 1000).toLocaleString()}</TableCell>
                                <TableCell className="text-center">{new Date(course.updatedAt * 1000).toLocaleString()}</TableCell>
                            </TableRow>
                        )) : <TableRow>
                            <TableCell colSpan={6} className="text-center py-4">Không có khóa học nào.</TableCell>
                        </TableRow>}
                    </TableBody>
                </Table>

                {pagination.totalPages > 0 && (
                    <Pagination>
                        <PaginationContent>
                            <PaginationItem>
                                <PaginationPrevious
                                    href="#"
                                    onClick={(event) => {
                                        event.preventDefault();
                                        handlePageChange(pagination.page - 1);
                                    }}
                                    className={pagination.page === 0 ? "pointer-events-none opacity-50" : undefined}
                                    aria-disabled={pagination.page === 0}
                                />
                            </PaginationItem>

                            {buildVisiblePages().map((pageItem, index) => (
                                <PaginationItem key={`${pageItem}-${index}`}>
                                    {pageItem === "ellipsis" ? (
                                        <PaginationEllipsis />
                                    ) : (
                                        <PaginationLink
                                            href="#"
                                            isActive={pageItem === pagination.page}
                                            onClick={(event) => {
                                                event.preventDefault();
                                                handlePageChange(pageItem);
                                            }}
                                        >
                                            {pageItem + 1}
                                        </PaginationLink>
                                    )}
                                </PaginationItem>
                            ))}

                            <PaginationItem>
                                <PaginationNext
                                    href="#"
                                    onClick={(event) => {
                                        event.preventDefault();
                                        handlePageChange(pagination.page + 1);
                                    }}
                                    className={pagination.page >= pagination.totalPages - 1 ? "pointer-events-none opacity-50" : undefined}
                                    aria-disabled={pagination.page >= pagination.totalPages - 1}
                                />
                            </PaginationItem>
                        </PaginationContent>
                    </Pagination>
                )}
            </>}
        </>
    );
}