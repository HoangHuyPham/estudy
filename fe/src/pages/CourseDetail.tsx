import { useEffect, useMemo, useState } from "react"
import { useNavigate, useParams } from "react-router"
import { AppRequest, Endpoint } from "@requests"
import { Button, Collapse, CollapseProps, Image, InputNumber, Rate, Tag } from "antd"
import { toast } from "react-toastify"
import { ICourse } from "@interfaces"
import moment from "moment"

type AddItemCartProps = {
    quantity: number,
    isSelected: boolean,
    productId: string,
    productOptionId?: string,
    orderId?: string,
    anonymous?: boolean
}

export const CourseDetail: React.FC<ICourse> = (data) => {
    const { productId } = useParams()
    // const { dispatchCartItems } = useCartItem()
    // const { dispatchCheckoutItems } = useCheckoutContext()
    const navigate = useNavigate()
    const [course, setCourse] = useState<ICourse|undefined>(undefined)

    const fetchCourse = async () => {
        const resp = await AppRequest.getInstance().get(`${Endpoint.GLOBAL_COURSE_URL}/${productId}`)
        const course: ICourse = resp.data?.data

        if (resp.status === 200) {
            setCourse(course)
        } else {
            toast.error(resp.statusText)
        }
    }

    useEffect(() => {
        fetchCourse()
    }, [data])

    useEffect(() => {
        // if (product?.productDetail && product?.productDetail.stock <= 0){
        //     setOutStock(true)
        // }
    },
        // [product]
    )

    // const addCartItem = async (product: IProductItem) => {
    //     const props: AddItemCartProps = {
    //         isSelected: true,
    //         productId: product.id,
    //         quantity,
    //         productOptionId: currentOption?.id
    //     }
    //     try {
    //         const { data, status } = await post({
    //             url: `${Endpoint.CART_URL}`,
    //             payload: { ...props }
    //         })

    //         if (status === 200) {
    //             toast.success(`Product ${product.name} is added to cart (x${quantity})`)

    //             dispatchCartItems({
    //                 type: CART_ACTION.INIT,
    //                 payload: data.cartItems
    //             })
    //         } else {
    //             toast.error(data)
    //         }
    //     } catch (error) {
    //         if (error?.status === 401) {
    //             toast.error(`You need to login to use this function.`)
    //         } else {
    //             toast.error(`Bug2!`)
    //         }
    //     }
    // }

    // const handleBuyNow = async (product: IProductItem) => {
    //     const props: AddItemCartProps = {
    //         isSelected: true,
    //         productId: product.id,
    //         quantity,
    //         productOptionId: currentOption?.id,
    //         anonymous: true
    //     }
    //     try {
    //         const { data, status } = await post({
    //             url: `${Endpoint.CART_URL}`,
    //             payload: { ...props }
    //         })

    //         if (status === 200) {
    //             toast.success(`Product ${product.name} is added to checkout (x${quantity})`)

    //             dispatchCheckoutItems({
    //                 type: CHECKOUT_ACTION.INIT,
    //                 payload: [data]
    //             })

    //             setTimeout(() => {
    //                 navigate("/checkout")
    //             }, 200);
    //         } else {
    //             toast.error(data)
    //         }
    //     } catch (error) {
    //         if (error?.status === 401) {
    //             toast.error(`You need to login to use this function.`)
    //         } else {
    //             toast.error(`Bug2!`)
    //         }
    //     }
    // }

    const formatPrice = (val: number | undefined) => {
        if (!val)
            return "undefined"
        const formatter = new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND',
            minimumFractionDigits: 0,
        })
        return formatter.format(val)
    }

    const totalLectures = useMemo(() => {
        return course?.sections?.reduce((sectionAcc, section) => {
            return sectionAcc + (section?.lectures?.length ?? 0)
        }, 0)
    }, [course])

    const totalDuration = useMemo(() => {
        const totalSeconds = course?.sections?.reduce((sectionAcc, section) => {
            return sectionAcc + (section.lectures?.reduce((lectureAcc, lecture) => {
                return lectureAcc + (lecture.video?.duration || 0);
            }, 0) || 0);
        }, 0) || 0;

        const duration = moment.duration(totalSeconds, 'seconds');

        const hours = duration.hours();
        const minutes = duration.minutes();
        const seconds = duration.seconds();
        return {
            hours, minutes, seconds
        }
    }, [course])

    const [sections, setSections] = useState<CollapseProps['items']>([])

    useEffect(() => {
        setSections(course?.sections?.map(v => ({
            key: v.ordinal,
            label: `Phần ${v.ordinal}: ${v.name}`,
            children: v?.lectures?.sort((v1, v2) => v1?.ordinal - v2?.ordinal).map((v) => (<p>{v.ordinal}. {v.title}</p>)),
        })).sort((v1, v2) => v1?.key - v2?.key))
    }, [course])

    return <>
        {
            course && (<div className='ProductDetail flex flex-col items-center p-5 gap-2 animate-fade w-[100%]'>
                <div className="flex">
                    <span className="flex flex-col">
                        <Image
                            className="shadow-xl"
                            key={course?.id}
                            width={420}
                            height={240}
                            src={course?.preview?.url}
                            fallback="data:image/png;base64,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"
                        />
                    </span>
                    <div className="flex flex-col gap-2 text-black p-5 font-semibold">
                        <span className="font-bold text-4xl">{course?.name}</span>
                        {/* <Rate disabled defaultValue={product?.productDetail?.totalRating} /> */}
                        <div className="flex justify-end"><span className="">Tác giả: <a className="text-blue-400" href="#">{course?.instructor?.displayName}</a></span></div>
                        <span className="text-2xl">Price: <span className="font-bold text-red-400">{formatPrice(course.currentPrice)}</span> <span className="line-through text-black text-xl">({formatPrice(course.oldPrice)})</span> </span>
                        <span>Description: {course?.description}</span>
                        <span>Ngôn ngữ: {course?.language}</span>
                        <span>Số phần: {course?.sections?.length} ({totalLectures} video)</span>
                        <span>Thời lượng khóa học: {totalDuration.hours} giờ {totalDuration.minutes} phút</span>
                        <span className="flex gap-5 p-5">
                            <Button type="primary" size={'large'} onClick={() => { }}>Thêm vào giỏ hàng</Button>
                            <Button type="primary" danger size={'large'} onClick={() => { }}>Mua ngay</Button>
                        </span>
                        <Collapse items={sections} />
                    </div>
                </div>
            </div>) || <p className="text-3xl font-bold text-center p-5">Sản phẩm không tồn tại 😢</p>
        }
    </>
}