import { Spin } from "antd";
import { ICourse } from "@interfaces"
import { LoadingOutlined } from "@ant-design/icons";

type CourseListProps = {
    courses: ICourse[] | undefined;
    loading: boolean | undefined,
}

export const CourseList: React.FC<CourseListProps> = ({ courses, loading}) => {
    return (
        <> 
            {courses && (<div className="CourseList flex flex-wrap w-[80%] gap-10">
                {
                    courses?.map((v) => <CourseI key={v.id} productItem={v} />)
                }
            </div>) || <Spin indicator={<LoadingOutlined spin />} size="large" spinning={true} />}

            {
                loading && <Spin indicator={<LoadingOutlined spin />} size="large" className="sticky bottom-0"></Spin>
            }
        </>
    )
}