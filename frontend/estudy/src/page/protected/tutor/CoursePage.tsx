import { Skeleton } from "@/component/base/skeleton";
import { Endpoints } from "@/constant";
import { CourseVisibilities, type ICourseSectionLectureVideo } from "@/interface";
import { axiosClient } from "@/lib/utils";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { FormProvider, useForm } from "react-hook-form";
import { CourseEdit } from "@/component/tutor/CourseEdit";
import { ResizableHandle, ResizablePanel, ResizablePanelGroup } from "@/component/base/resizable";
import { SectionEdit } from "@/component/tutor/SectionEdit";
import { FloatSave } from "@/component/tutor/FloatSave";
import { CourseEditPreview } from "@/component/tutor/CourseEditPreview";
import * as z from "zod";
import { zodResolver } from "@hookform/resolvers/zod";

const CourseSchema = z.object({
    id: z.string(),
    name: z.string().min(1, "Tên khóa học không được để trống"),
    description: z.string().optional(),
    visibility: z.enum(Object.values(CourseVisibilities)).optional(),
    sections: z.array(z.object({
        id: z.string(),
        name: z.string().min(1, "Tên chương học không được để trống"),
        weight: z.number().optional(),
        lectures: z.array(z.object({
            id: z.string(),
            name: z.string().min(1, "Tên bài giảng không được để trống"),
            weight: z.number().optional(),
        })).optional(),
    })).optional(),
})

export const CoursePage: React.FC = () => {
    const [loading, setLoading] = useState(false);
    const params = useParams();
    const form = useForm<z.infer<typeof CourseSchema>>({
        mode: "all",
        resolver: zodResolver(CourseSchema),
    });

    useEffect(() => {
        const abortController = new AbortController();

        const fetchCourses = async () => {
            setLoading(true);
            try {
                const response = await axiosClient.get(`${Endpoints.TUTOR.COURSES}/${params.id}`, {
                    signal: abortController.signal,
                });
                const { data } : { data: Partial<ICourseSectionLectureVideo> } = response.data;
                data.sections = data.sections?.sort((s1, s2) => s1.weight - s2.weight)
                data.sections?.forEach(section => {
                    section.lectures = section.lectures?.sort((l1, l2) => l1.weight - l2.weight);
                });
                form.reset(data);
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
    }, []);

    console.log("Re render", "CoursePage");

    return (
        <>
            <h1 className="text-2xl font-bold px-2 pt-5">Chỉnh sửa khóa học</h1>
            {loading && <Skeleton className="h-full w-full" />}
            <FormProvider {...form}>
                <FloatSave />
                <ResizablePanelGroup
                    orientation="horizontal"
                    className="flex grow justify-between rounded-lg border-2 border-dashed border-muted">
                    <ResizablePanel className="p-5" defaultSize="70%">
                        <section className="flex flex-col gap-5 mb-4">
                            <CourseEdit />
                        </section>

                        <section className="flex flex-col gap-5 mb-4">
                            <SectionEdit />
                        </section>
                    </ResizablePanel>
                    <ResizableHandle withHandle />
                    <ResizablePanel className="p-5" minSize="20%" maxSize="40%" defaultSize="30%">
                        <h1 className="py-2">Preview</h1>
                        <CourseEditPreview />
                    </ResizablePanel>
                </ResizablePanelGroup>
            </FormProvider>
        </>
    );
}