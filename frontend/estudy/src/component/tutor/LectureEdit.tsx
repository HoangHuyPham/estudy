import { useFieldArray, useFormContext, useFormState } from 'react-hook-form';
import { Card, CardContent } from '@/component/base/card';
import { RiAddLine, RiArrowDownLine, RiArrowUpLine } from '@remixicon/react';
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from '@/component/base/collapsible';
import { Button } from '@/component/base/button';
import { Field, FieldError, FieldLabel } from '@/component/base/field';
import { Input } from '@/component/base/input';
import { Badge } from '@/component/base/badge';
import type { ICourseSectionLecture, ILecture, ISectionLecture } from '@/interface';
import { cn } from '@/lib/utils';

export const LectureEdit: React.FC<{ sectionIndex: number }> = ({ sectionIndex }) => {

    const { register, control, getValues } = useFormContext();
    const { defaultValues, errors } = useFormState<Partial<ICourseSectionLecture>>({ control });
    const lectures = useFieldArray({ control, name: `sections.${sectionIndex}.lectures`, keyName: "_key" });

    console.log("Re render", "LectureEdit", sectionIndex);

    const handleAddLecture = () => {
        const lastWeight: number = getValues(`sections.${sectionIndex}.lectures.${lectures.fields.length - 1}.weight`) || 0;
        lectures.append({ name: "Bài giảng mới", weight: lastWeight + 10000, _created: crypto.randomUUID(), _section_id: getValues(`sections.${sectionIndex}.id`)});
    }

    const handleMoveUp = (lectureIndex: number) => {
        if (lectureIndex === 0) return;
        const currentLecture = getValues(`sections.${sectionIndex}.lectures.${lectureIndex}`);
        const aboveLecture = getValues(`sections.${sectionIndex}.lectures.${lectureIndex - 1}`);
        lectures.update(lectureIndex, { ...aboveLecture, weight: currentLecture.weight, _section_id: getValues(`sections.${sectionIndex}.id`)});
        lectures.update(lectureIndex - 1, { ...currentLecture, weight: aboveLecture.weight, _section_id: getValues(`sections.${sectionIndex}.id`)});
    }

    const handleMoveDown = (lectureIndex: number) => {
        if (lectureIndex === lectures.fields.length - 1) return;
        const currentLecture = getValues(`sections.${sectionIndex}.lectures.${lectureIndex}`);
        const belowLecture = getValues(`sections.${sectionIndex}.lectures.${lectureIndex + 1}`);
        lectures.update(lectureIndex, { ...belowLecture, weight: currentLecture.weight, _section_id: getValues(`sections.${sectionIndex}.id`) });
        lectures.update(lectureIndex + 1, { ...currentLecture, weight: belowLecture.weight, _section_id: getValues(`sections.${sectionIndex}.id`) });
    }

    const hasChanged = (lectureIndex: number) => {
        const currentLecture = getValues(`sections.${sectionIndex}.lectures.${lectureIndex}`);
        const currentSection = getValues(`sections.${sectionIndex}`);
        const defaultLectureBySectionId = (defaultValues?.sections as ISectionLecture[])?.find((section) => section.id === currentSection.id)?.lectures?.find((lecture) => lecture.id === currentLecture.id);
        return Object.keys(defaultLectureBySectionId || {}).some((key) => {
            return currentLecture[key] !== defaultLectureBySectionId?.[key as keyof ILecture];
        })
    }

    return (
        <>
            <Collapsible defaultOpen>
                <CollapsibleTrigger asChild>
                    <Button className="group bg-primary flex justify-between w-full px-2 cursor-pointer">
                        <FieldLabel>Bài giảng ({lectures.fields.length})</FieldLabel>
                        <RiArrowDownLine className="ml-auto group-data-[state=open]:rotate-180" />
                    </Button>
                </CollapsibleTrigger>
                <CollapsibleContent>
                    {
                        lectures.fields.map((lecture, lectureIndex) => (
                            // eslint-disable-next-line @typescript-eslint/no-explicit-any
                            <Card key={lecture._key} className={cn("w-full", (lecture as any)?._created && "animate-slightly-scale")}>
                                <CardContent className="flex flex-col gap-5">
                                    <Field data-disabled>
                                        <div className="flex justify-between items-center gap-5">
                                            <div className="flex gap-2">
                                                <FieldLabel htmlFor="textarea-disabled" >
                                                    Bài giảng {lectureIndex + 1}
                                                </FieldLabel>
                                                {
                                                    getValues(`sections.${sectionIndex}.lectures.${lectureIndex}.id`) === undefined && <Badge className="bg-orange-600">Mới</Badge>
                                                }
                                                {
                                                    hasChanged(lectureIndex) && <Badge className="bg-blue-600">Thay đổi</Badge>
                                                }
                                            </div>
                                            <div>
                                                <Button variant="default" size="sm" onClick={() => handleMoveUp(lectureIndex)} disabled={lectureIndex === 0}>
                                                    <RiArrowUpLine />
                                                </Button>
                                                <Button variant="default" size="sm" onClick={() => handleMoveDown(lectureIndex)} disabled={lectureIndex === lectures.fields.length - 1}>
                                                    <RiArrowDownLine />
                                                </Button>
                                                <Button variant="destructive" size="sm" onClick={() => lectures.remove(lectureIndex)}>
                                                    <RiAddLine className="rotate-45" />
                                                </Button>
                                            </div>
                                        </div>
                                        <FieldError errors={[errors?.sections?.[sectionIndex]?.lectures?.[lectureIndex]?.name]} />
                                        <Input placeholder="Tên bài giảng, tên được hiển thị cho bài giảng" {...register(`sections.${sectionIndex}.lectures.${lectureIndex}.name`)} />
                                    </Field>
                                </CardContent>
                            </Card>
                        ))
                    }
                    <div className="flex gap-5">
                        <Card className="flex grow border-dashed border-2 border-primary">
                            <CardContent>
                                <Button variant="outline" size="lg" className="w-full" onClick={handleAddLecture}>
                                    <RiAddLine className="mr-1" />
                                    Thêm bài giảng mới
                                </Button>
                            </CardContent>
                        </Card>
                    </div>
                </CollapsibleContent>
            </Collapsible>
        </>
    )
}