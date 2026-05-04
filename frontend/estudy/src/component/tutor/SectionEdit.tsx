import { useFieldArray, useFormContext, useFormState } from 'react-hook-form';
import { Card, CardContent, CardHeader } from '@/component/base/card';
import { RiAddLine, RiArrowDownLine, RiArrowUpLine } from '@remixicon/react';
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from '@/component/base/collapsible';
import { Button } from '@/component/base/button';
import { Field, FieldError, FieldLabel } from '@/component/base/field';
import { Input } from '@/component/base/input';
import { LectureEdit } from '@/component/tutor/LectureEdit';
import { Badge } from '@/component/base/badge';
import type { ICourseSectionLecture, ISection } from '@/interface';
import { cn } from '@/lib/utils';

export const SectionEdit: React.FC = () => {

    const { register, control, getValues } = useFormContext();
    const sections = useFieldArray({ control, name: `sections`, keyName: "_key" });
    const { dirtyFields, errors } = useFormState<Partial<ICourseSectionLecture>>({ control });

    const handleAddSection = () => {
        const lastWeight: number = getValues(`sections.${sections.fields.length - 1}.weight`) || 0;
        sections.append({ name: "Chương học mới", weight: lastWeight + 10000, _created: crypto.randomUUID(), _course_id: getValues(`id`) });
    }

    const handleMoveUp = (index: number) => {
        if (index === 0) return;
        const currentSection: ISection = getValues(`sections.${index}`);
        const aboveSection: ISection = getValues(`sections.${index - 1}`);
        sections.update(index, { ...aboveSection, weight: currentSection.weight, _course_id: getValues(`id`) });
        sections.update(index - 1, { ...currentSection, weight: aboveSection.weight, _course_id: getValues(`id`) });
    }

    const handleMoveDown = (index: number) => {
        if (index === sections.fields.length - 1) return;
        const currentSection: ISection = getValues(`sections.${index}`);
        const belowSection: ISection = getValues(`sections.${index + 1}`);
        sections.update(index, { ...belowSection, weight: currentSection.weight, _course_id: getValues(`id`) });
        sections.update(index + 1, { ...currentSection, weight: belowSection.weight, _course_id: getValues(`id`) });
    }

    const hasChanged: (index: number) => boolean = (index) => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars,@typescript-eslint/no-explicit-any
        const { lectures, _course_id, ...sectionData } = dirtyFields?.sections?.[index] as any || {};
        return Object.keys(sectionData || {}).some((key) => !!sectionData?.[key as keyof ISection] && !sectionData?.["_created"]);
    }

    console.log("Re render", "SectionEdit");

    return (<Collapsible defaultOpen>
        <CollapsibleTrigger asChild>
            <Button className="group bg-primary flex justify-between w-full px-2 cursor-pointer">
                <FieldLabel>Chương học ({sections.fields.length})</FieldLabel>
                <RiArrowDownLine className="ml-auto group-data-[state=open]:rotate-180" />
            </Button>
        </CollapsibleTrigger>
        <CollapsibleContent>
            {sections.fields.map((section, index) => (
                // eslint-disable-next-line @typescript-eslint/no-explicit-any
                <Card key={section._key} className={cn("flex grow", (section as any)?._created && "animate-slightly-scale")}>
                    <CardHeader>
                        <Field data-disabled>
                            <div className={"flex justify-between items-center gap-5"}>
                                <div className="flex gap-2">
                                    <FieldLabel htmlFor="textarea-disabled" >
                                        Chương {index + 1}
                                    </FieldLabel>
                                    {
                                        !!getValues(`sections.${index}._created`) && <Badge className="bg-orange-600">Mới</Badge>
                                    }
                                    {
                                        hasChanged(index) && <Badge className="bg-blue-600">Thay đổi</Badge>
                                    }
                                </div>
                                <div>
                                    <Button variant="default" size="sm" onClick={() => handleMoveUp(index)} disabled={index === 0}>
                                        <RiArrowUpLine />
                                    </Button>
                                    <Button variant="default" size="sm" onClick={() => handleMoveDown(index)} disabled={index === sections.fields.length - 1}>
                                        <RiArrowDownLine />
                                    </Button>
                                    <Button variant="destructive" size="sm" onClick={() => sections.remove(index)}>
                                        <RiAddLine className="rotate-45" />
                                    </Button>
                                </div>
                            </div>
                            <FieldError errors={[errors?.sections?.[index]?.name]} />
                            <Input placeholder="Tên chương học, tên được hiển thị cho chương học" {...register(`sections.${index}.name`)} />
                        </Field>
                    </CardHeader>
                    <CardContent className="ml-[10%]">
                        <LectureEdit sectionIndex={index} />
                    </CardContent>
                </Card>
            ))}
            <div className="flex gap-5">
                <Card className="flex grow border-dashed border-2 border-primary">
                    <CardContent>
                        <Button variant="outline" size="lg" className="w-full" onClick={handleAddSection}>
                            <RiAddLine className="mr-1" />
                            Thêm chương học
                        </Button>
                    </CardContent>
                </Card>
            </div>
        </CollapsibleContent>
    </Collapsible>)
}