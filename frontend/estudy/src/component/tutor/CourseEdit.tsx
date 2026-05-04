import { useFormContext, useFormState } from 'react-hook-form';
import { Field, FieldContent, FieldGroup, FieldLabel, FieldSeparator } from '@/component/base/field';
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from '@/component/base/collapsible';
import { RiArrowDownLine } from '@remixicon/react';
import { Button } from '../base/button';
import { Card, CardContent } from '@/component/base/card';
import { Input } from '@/component/base/input';
import { CourseVisibilities } from '@/interface';
import { Label } from '@/component/base/label';
import { Badge } from '@/component/base/badge';
import { NativeSelect, NativeSelectOption } from '@/component/base/native-select';

export const CourseEdit: React.FC = () => {

    const { register, control, getValues } = useFormContext();
    const { isDirty, dirtyFields } = useFormState({ control });

    console.log("Re render", "CourseEdit");

    const isCourseChanged: () => boolean = () => {
        if (!isDirty) return false;
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const { sections, ...courseChanges } = dirtyFields;
        if (Object.keys(courseChanges).length === 0) return false;
        return true;
    }

    return (
        <Collapsible defaultOpen>
            <CollapsibleTrigger asChild>
                <Button className="group bg-primary flex justify-between w-full px-2 cursor-pointer">
                    <FieldLabel>Thông tin chung</FieldLabel>
                    <RiArrowDownLine className="ml-auto group-data-[state=open]:rotate-180" />
                </Button>
            </CollapsibleTrigger>
            <CollapsibleContent>
                <Card className="w-full">
                    <CardContent className="flex flex-col gap-5">
                        <FieldGroup>
                            <Field data-disabled>
                                <FieldContent className="flex flex-row justify-between gap-5 text-muted-foreground">
                                    <div>
                                        <div>Trạng thái: {"DRAFT"}</div>
                                    </div>
                                    <div className='flex flex-row gap-5'>
                                        <Label>Ngày tạo: {new Date((getValues("createdAt") || 0) * 1000).toLocaleString()}</Label>
                                        <Label>Lần cập nhật cuối: {new Date((getValues("updatedAt") || 0) * 1000).toLocaleString()}</Label>
                                    </div>
                                </FieldContent>
                            </Field>
                            <FieldSeparator />
                            <Field>
                                <FieldLabel>
                                    Hiển thị {isCourseChanged() && <Badge className="bg-blue-600">Thay đổi</Badge>}
                                </FieldLabel>
                            </Field>
                            <Field data-disabled>
                                <FieldLabel htmlFor="textarea-disabled">Tên khóa học</FieldLabel>
                                <Input placeholder="Tên khóa học, tên được hiển thị cho khóa học" {...register("name")} />
                            </Field>
                            <Field data-disabled>
                                <FieldLabel htmlFor="textarea-disabled">Mô tả chung</FieldLabel>
                                <Input placeholder="Nội dung mô tả chung của khóa học" {...register("description")} />
                            </Field>
                            <Field data-disabled>
                                <FieldLabel htmlFor="textarea-disabled">Thumbnail</FieldLabel>
                                <div className="flex gap-2">
                                    <Input disabled className="flex-[70%]" placeholder="Nội dung mô tả chung của khóa học" value={getValues("thumbnail")?.startsWith("blob:") ? getValues("thumbnail").substring(getValues("thumbnail").length - 36, (getValues("thumbnail") as string).length) : getValues("thumbnail")} />
                                </div>
                            </Field>
                            <FieldSeparator />
                            <Field>
                                <FieldLabel>Chế độ hiển thị</FieldLabel>
                            </Field>
                            <Field data-disabled>
                                <FieldLabel htmlFor="textarea-disabled">Chế độ hiển thị</FieldLabel>
                                <NativeSelect className="w-full" {...register("visibility")}>
                                    {Object.values(CourseVisibilities).map((value) => (
                                        <NativeSelectOption key={value} value={value}>
                                            {value}
                                        </NativeSelectOption>
                                    ))}
                                </NativeSelect>
                            </Field>
                            <FieldSeparator />
                            <Field>
                                <FieldLabel>Giá khóa học</FieldLabel>
                            </Field>
                            <FieldContent>
                                <Field data-disabled>
                                    <FieldLabel htmlFor="textarea-disabled">Giá gốc</FieldLabel>
                                    <Input {...register("oldPrice", { valueAsNumber: true })} />
                                </Field>
                                <Field data-disabled>
                                    <FieldLabel htmlFor="textarea-disabled">Giá hiện tại</FieldLabel>
                                    <Input {...register("price", { valueAsNumber: true })} />
                                </Field>
                            </FieldContent>
                        </FieldGroup>
                    </CardContent>
                </Card>
            </CollapsibleContent>
        </Collapsible>)
}