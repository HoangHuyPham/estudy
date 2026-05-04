import { RiAddCircleLine, RiLoader2Line, RiSave2Line } from "@remixicon/react";
import { Floatter } from "../Floatter";
import { Button } from '@/component/base/button';
import { useFormContext, useFormState } from "react-hook-form";
import type { ICourse, ICourseSectionLecture, ILecture, ISection, ISectionLecture } from "@/interface";
import { axiosClient } from "@/lib/utils";
import { Endpoints } from "@/constant";
import { useState } from "react";
import { toast } from "sonner";
import { useNavigate } from "react-router-dom";

export const FloatSave: React.FC = () => {
    const [loading, setLoading] = useState(false);
    const { control, getValues, reset } = useFormContext();
    const { isDirty, dirtyFields, defaultValues, errors } = useFormState<Partial<ICourseSectionLecture>>({ control });
    const navigate = useNavigate()

    console.log("Re render", "FloatSave");

    const handleSave = () => {
        setLoading(true);

        const updateCourseThumbnail = async () => {
            const thumbnail = getValues("thumbnail");
            if (thumbnail && (thumbnail as string).startsWith("blob:")) {
                const formData = new FormData();
                const response = await fetch(thumbnail);
                const blob = await response.blob();
                formData.append("file", blob, `${thumbnail}`);

                try {
                    await axiosClient.post(Endpoints.TUTOR.COURSES + `/${getValues("id")}/addThumbnail`, formData, {
                        headers: {
                            "Content-Type": "multipart/form-data",
                        },
                    });
                } catch (error) {
                    toast.error("Failed to upload course thumbnail", { richColors: true });
                    console.error("Failed to upload course thumbnail", error);
                }
            }
        }

        const updateCourse = async () => {
            // eslint-disable-next-line @typescript-eslint/no-unused-vars
            const { thumbnail, ...updatedCourse } = getUpdatedCourses() || {};
            try {
                if (updatedCourse)
                    await axiosClient.patch(Endpoints.TUTOR.COURSES, {
                        courses: [updatedCourse],
                    });
            } catch (error) {
                toast.error("Failed to update course", { richColors: true });
                console.error("Failed to update course", error);
            }
        }

        const createSections = async () => {
            const createdSections = getCreatedSections();
            try {
                if (createdSections.length > 0)
                    await axiosClient.post(Endpoints.TUTOR.SECTIONS, {
                        courseId: getValues("id"),
                        sections: createdSections.map(section => {
                            const { name, weight, lectures } = section;

                            return {
                                name, weight, lectures: lectures.map(lecture => {
                                    const { name, weight } = lecture;
                                    return { name, weight };
                                })
                            };
                        })
                    });
            } catch (error) {
                toast.error("Failed to create section(s)", { richColors: true });
                console.error("Failed to create section(s)", error);
            }
        }

        const updateSections = async () => {
            const updatedSections = getUpdatedSections();
            try {
                if (updatedSections.length > 0)
                    await axiosClient.patch(Endpoints.TUTOR.SECTIONS, {
                        sections: updatedSections.map(section => {
                            const { id, name, weight } = section;
                            return { id, name, weight };
                        })
                    });
            } catch (error) {
                toast.error("Failed to update sections", { richColors: true });
                console.error("Failed to update sections", error);
            }
        }

        const removeSections = async () => {
            const removedSections = getRemovedSections();
            try {
                if (removedSections.length > 0)
                    await axiosClient.post(Endpoints.TUTOR.SECTIONS_BULK_DELETE, {
                        sections: removedSections
                    });
            } catch (error) {
                toast.error("Failed to update sections", { richColors: true });
                console.error("Failed to update sections", error);
            }
        }

        const createLecturesOnSections = async () => {
            const createdLectures = getCreatedLecturesHasSections();
            // eslint-disable-next-line @typescript-eslint/no-explicit-any
            const sectionMap = {} as any;
            createdLectures.forEach(lecture => {
                // eslint-disable-next-line @typescript-eslint/no-explicit-any
                const sectionId = (lecture as any)._section_id;
                if (!sectionMap[sectionId]) {
                    sectionMap[sectionId] = [];
                }
                sectionMap[sectionId].push(lecture);
            })

            const requests = Object.keys(sectionMap).map(async (sectionId) => {
                try {
                    await axiosClient.post(Endpoints.TUTOR.LECTURES, {
                        courseId: getValues("id"),
                        sectionId,
                        // eslint-disable-next-line @typescript-eslint/no-explicit-any
                        lectures: sectionMap[sectionId].map(({ name, weight }: any) => ({ name, weight }))
                    });
                } catch (error) {
                    // Bắt lỗi cho từng section cụ thể
                    console.error(`Failed to create lectures for section ${sectionId}`, error);
                    throw error; // Quăng lỗi để Promise.all bắt được
                }
            });

            await Promise.all(requests)
                .then(() => {
                    toast.success("Lectures created successfully", { richColors: true });
                })
                .catch(() => {
                    toast.error("Failed to create lecture(s)", { richColors: true });
                });
        }

        const updateLectures = async () => {
            const updatedLectures = getUpdatedLectures();
            try {
                if (updatedLectures.length > 0)
                    await axiosClient.patch(Endpoints.TUTOR.LECTURES, {
                        lectures: updatedLectures.map(lecture => {
                            const { id, name, weight } = lecture;
                            return { id, name, weight };
                        })
                    });
            } catch (error) {
                toast.error("Failed to update lectures", { richColors: true });
                console.error("Failed to update lectures", error);
            }
        }

        const removeLectures = async () => {
            const removedLectures = getRemovedLectures();
            try {
                if (removedLectures.length > 0)
                    await axiosClient.post(Endpoints.TUTOR.LECTURES_BULK_DELETE, {
                        lectures: removedLectures
                    });
            } catch (error) {
                toast.error("Failed to delete lectures", { richColors: true });
                console.error("Failed to delete lectures", error);
            }
        }

        Promise.all([
            updateCourse().then(() => updateCourseThumbnail()),
            createSections(),
            updateSections(),
            createLecturesOnSections(),
            updateLectures()])
            .then(async () => {
                await removeLectures()
                await removeSections()
                toast.success("Course updated successfully", { richColors: true });
                navigate(0)
            })
            .finally(() => {
                setLoading(false);
            })
    }


    const handleAbort = () => {
        reset();
    }

    const getUpdatedCourses: () => Partial<ICourse> | undefined = () => {
        if (!isDirty) return undefined;
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const { thumbnail, sections, ...courseChanges } = dirtyFields;
        if (Object.keys(courseChanges).length === 0) return undefined;
        const updatedCourse = Object.keys(courseChanges).reduce((updatedCourse, key) => {
            updatedCourse[key as keyof Partial<ICourse>] = getValues(key);
            return updatedCourse;
        }, {} as Partial<ICourse>);
        updatedCourse.id = defaultValues?.id;
        return updatedCourse;
    }

    const getRemovedSections: () => string[] = () => {
        if (!isDirty) return [];
        const originalSections: ISection[] = defaultValues?.sections as ISection[];
        const currentSections: ISection[] = getValues("sections");

        const removedSections = originalSections?.filter(originalSection => !currentSections?.some(currentSection => currentSection.id === originalSection.id));
        return removedSections?.map(section => section.id);
    }

    const getCreatedSections: () => ISectionLecture[] = () => {
        if (!isDirty) return [];
        return (getValues("sections") as ISectionLecture[])?.filter(
            // eslint-disable-next-line @typescript-eslint/no-explicit-any
            (section) => !!(section as any)._created
        );
    }

    const getUpdatedSections: () => Partial<ISection>[] = () => {
        if (!isDirty) return [];

        const currentSections = getValues("sections") as ISection[];
        const originalSections = defaultValues?.sections as ISection[];

        const updatedSections: Partial<ISection>[] = [];
        currentSections.forEach(section => {
            const originalSection = originalSections?.find(orig => orig.id === section.id);
            if (!originalSection) return;
            const updatedSection = {} as Partial<ISection>;
            Object.keys(originalSection).forEach(key => {
                if (section[key as keyof ISection] !== originalSection[key as keyof ISection] && key !== "lectures") {
                    // eslint-disable-next-line @typescript-eslint/no-explicit-any
                    updatedSection[key as keyof Partial<ISection>] = section[key as keyof ISection] as any;
                }
            })
            if (Object.keys(updatedSection).length > 0) {
                updatedSection.id = section.id;
                updatedSections.push(updatedSection);
            }
        })

        return updatedSections.length > 0 ? updatedSections : [];
    }

    const getRemovedLectures: () => string[] = () => {
        if (!isDirty) return [];
        const currentLectureIds: string[] = (getValues("sections") as ISectionLecture[])?.flatMap((section) => {
            return (section as ISectionLecture)?.lectures?.map(lecture => lecture.id) || [];
        })

        const originalLectureIds: string[] = (defaultValues?.sections as ISectionLecture[])?.flatMap((section) => {
            return (section as ISectionLecture)?.lectures?.map(lecture => lecture.id) || [];
        })
        return originalLectureIds?.filter(originalLectureId => !currentLectureIds?.some(currentLectureId => currentLectureId === originalLectureId));
    }

    const getCreatedLecturesHasSections: () => ILecture[] = () => {
        if (!isDirty) return [];
        return (getValues("sections") as (ISectionLecture)[])?.flatMap(section =>
            // eslint-disable-next-line @typescript-eslint/no-explicit-any
            section?.lectures?.filter(lecture => !!(lecture as any)._created && !!(lecture as any)._section_id)) || [];
    }

    const getUpdatedLectures: () => Partial<ILecture>[] = () => {
        if (!isDirty) return [];
        const currentLectures = (getValues("sections") as ISectionLecture[])?.flatMap(section => section?.lectures || []) || [];
        const originalLectures = (defaultValues?.sections as ISectionLecture[])?.flatMap(section => section?.lectures || []) || [];

        const updatedLectures: Partial<ILecture>[] = [];

        currentLectures.forEach(lecture => {
            const originalLecture = originalLectures.find(orig => orig.id === lecture.id);
            if (!originalLecture) return;
            const updatedLecture = {} as Partial<ILecture>;
            Object.keys(originalLecture).forEach(key => {
                if (lecture[key as keyof ILecture] !== originalLecture[key as keyof ILecture]) {
                    // eslint-disable-next-line @typescript-eslint/no-explicit-any
                    updatedLecture[key as keyof Partial<ILecture>] = lecture[key as keyof ILecture] as any;
                }
            })
            if (Object.keys(updatedLecture).length > 0) {
                updatedLecture.id = lecture.id;
                updatedLectures.push(updatedLecture);
            }
        })

        return updatedLectures.length > 0 ? updatedLectures : [];
    }

    return (
        <Floatter className="h-15">
            <div className="flex flex-row-reverse my-2">
                <div className="flex gap-2">
                    <Button className="bg-red-500 hover:bg-red-600" size="lg" disabled={!isDirty || (Object.keys(errors).length > 0) || loading} onClick={handleAbort}>
                        {loading ? <RiLoader2Line className="animate-spin" /> : <RiAddCircleLine className="mr-1 rotate-45" />}
                        Bỏ thay đổi
                    </Button>

                    <Button variant="default" size="lg" disabled={!isDirty || (Object.keys(errors).length > 0) || loading} onClick={handleSave}>
                        {loading ? <RiLoader2Line className="animate-spin" /> : <RiSave2Line className="mr-1" />}
                        Lưu
                    </Button>
                </div>
            </div>
        </Floatter>
    )
}