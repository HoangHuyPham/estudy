import { useFormContext, useFormState, useWatch } from 'react-hook-form';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/component/base/card';
import { Endpoints } from '@/constant';
import type { ICourseSectionLecture } from '@/interface';
import { useAppStore } from '@/store/AppStore';
import { AspectRatio } from '@/component/base/aspect-ratio';
import { useEffect } from 'react';

export const CourseEditPreview: React.FC = () => {
    const { control, setValue } = useFormContext();
    const { isReady } = useFormState({ control });
    const course: ICourseSectionLecture = useWatch({ control }) as ICourseSectionLecture
    const userData = useAppStore(state => state.userData);
    const thumbnail = useWatch({ control, name: "thumbnail" });

    console.log("Re render", "CourseEditPreview");

    const handleUploadCourseThumbnail = async () => {
        try {
            const [fileHandle] = await window.showOpenFilePicker({
                types: [
                    {
                        accept: {
                            "image/png": [".png"],
                            "image/jpeg": [".jpg", ".jpeg"],
                        }
                    },

                ],
                startIn: "pictures",
                excludeAcceptAllOption: false,
                multiple: false,
            })
            const img = await fileHandle.getFile()
            const tempUrl = URL.createObjectURL(img);
            setValue("thumbnail", tempUrl, {shouldDirty: true});
        } catch (error) {
            console.error("Error occurred while uploading course thumbnail:", error);
        }
    }

    useEffect(() => {
        return () => {
            if (thumbnail && (thumbnail as string).startsWith("blob:")) {
                URL.revokeObjectURL(thumbnail);
                console.log("revoked", thumbnail);
            }
        }
    }, [thumbnail])

    return (
        <>
            {
                isReady && (<Card className="cursor-pointer relative pt-0 overflow-hidden opacity-80 hover:opacity-100 transition-opacity">
                    <div className="relative w-full">
                        <div onClick={handleUploadCourseThumbnail} className="absolute w-full text-xl text-center h-full top-0 z-5 hover:bg-muted flex items-center justify-center opacity-0 hover:opacity-80 transition-opacity">
                            Thay đổi thumbnail cho khóa học
                        </div>
                        <AspectRatio ratio={16 / 9}>
                            {
                                (thumbnail && (thumbnail as string).startsWith("blob:")) ? <img src={thumbnail} alt={course.name} className="rounded w-full h-full object-fit" /> :
                                    <img src={`${Endpoints.BACKEND}${Endpoints.SERVE}/${course.thumbnail}`} alt={course.name} className="rounded w-full h-full object-fit" />
                            }
                        </AspectRatio>
                    </div>

                    <CardHeader>
                        <CardTitle className="line-clamp-2 text-xl font-bold">{course?.name}</CardTitle>
                        <CardDescription className="line-clamp-3">{course?.description}</CardDescription>
                    </CardHeader>
                    <CardContent>
                        <CardDescription>bởi {userData?.displayName}</CardDescription>
                        <CardDescription className="text-xl font-bold">
                            {
                                course?.price === 0 ? <span>Miễn phí</span> :
                                    <>
                                        <span>{course?.price?.toLocaleString("vi-VN", { style: "currency", currency: "VND" })}</span>
                                        {course?.oldPrice > course?.price && (<span className="ml-2 text-base font-normal text-muted-foreground line-through">{course?.oldPrice?.toLocaleString("vi-VN", { style: "currency", currency: "VND" })}</span>)}
                                    </>
                            }
                        </CardDescription>
                        <CardDescription>Thời lượng: {course?.duration}</CardDescription>
                        <CardDescription>Số người tham gia: {course?.enrollmentCount}</CardDescription>
                    </CardContent>
                </Card>)
            }
        </>
    )
}