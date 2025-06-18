import { useEffect, useState } from 'react'
import { Carousel, Dropdown, Input, InputNumber, MenuProps, Pagination, Space, Switch, Typography } from 'antd';
import { CourseList } from '@components';
import { DownOutlined } from '@ant-design/icons';
import { Endpoint, AppRequest } from '@requests';
import { useDebounce, useUserContext } from '@hooks';
import { toast } from 'react-toastify';
// import { USER_ACTION } from '../../contexts/UserContext';
import { ICourse } from '@interfaces';
import { AxiosError } from 'axios';

export const Home: React.FC = () => {
  const [currentPage, setCurrentPage] = useState(0)
  const [pageSize, setPageSize] = useState(10)
  const [totalItems, setTotalItems] = useState(0)

  const [courses, setCourses] = useState<ICourse[]>([])
  const [categoryId, setCategoryId] = useState<string>("3381e3d3-1599-4c73-885f-e8cda0c4ca99")
  const { dispatchUser } = useUserContext()
  const [loading, setLoading] = useState(false)

  const [minPrice, setMinPrice] = useState(0)
  const [maxPrice, setMaxPrice] = useState(1_000_000_000)
  const [keyword, setKeyword] = useState<string>();
  const keywordDebounced = useDebounce(keyword, 1000)

  const [alphabetSort, setAlphabetSort] = useState(false)
  const [priceSort, setPriceSort] = useState(false)

  useEffect(() => {
    try {
      setLoading(true)
      fetchCourses()
    } catch (error) {
      console.warn(error)
    } finally {
      setTimeout(() => {
        setLoading(false)
      }, 100);
    }
  }, [pageSize, currentPage, categoryId, keywordDebounced, minPrice, maxPrice])

  // useEffect(() => {
  //   fetchUser()
  //   fetchCategories()
  // }, [])

  const fetchUser = async () => {
    // try {
    //   const { status, data } = await get({
    //     url: Endpoint.PROFILE_URL
    //   })

    //   if (status === 200) {
    //     dispatchUser({
    //       type: USER_ACTION.ADD,
    //       payload: data as IUser
    //     })
    //   } else {
    //     toast.error(`Error: ${data}`)
    //   }
    // } catch (error) {
    //   console.error(`Error: ${error}`)
    // }
  }

  const fetchCourses = async () => {
    const params = {
      minPrice,
      maxPrice,
      page: currentPage,
      limit: pageSize,
      keyword: keywordDebounced
    }
    try {
      const resp = await AppRequest.getInstance().get(Endpoint.GLOBAL_COURSE_URL, {
        params
      })

      if (resp.status === 200) {
        const courses: ICourse[] = resp.data?.data
        const total: number = resp.data?.total
        setCourses(courses)
        setTotalItems(total)
      }
    } catch (err) {
      const error = err as AxiosError
      toast.error(error.message)
      setTotalItems(0)
      setCourses([])
    }
  }

  useEffect(()=>{
    let result:ICourse[] = courses
    if (priceSort){
        result = result?.sort((c1, c2)=>(c1?.currentPrice ??0) - (c2?.currentPrice ??0))
    }

    if (alphabetSort){
        result = result?.sort((c1, c2)=>(c1?.name || '').localeCompare(c2?.name || ''))
    }

    if (!priceSort && !alphabetSort){
      fetchCourses()
    }

    setCourses([...result])
  }, [priceSort, alphabetSort])

  const handlePagination = (page: number, pageSize: number) => {
    setPageSize(pageSize)
    setCurrentPage(page - 1)
  }

  return (
    <>
      <div className='Home flex flex-col items-center py-5 gap-5'>
        <div className="min-w-[1024px] max-w-[1024px]">
          <Carousel autoplay>
            <div className='flex justify-center'>
              <img lazy-loading={true} className='mx-auto' src='/banner1.png' />
            </div>
            <div className='flex justify-center'>
              <img lazy-loading={true} className='mx-auto' src='/banner2.png' />
            </div>
          </Carousel>
        </div>

        <div className="FilterWrapper flex gap-5 items-center">
          <div className="SearchWrapper w-[240px]">
            <Input onChange={(e) => setKeyword(e?.target?.value)} size="small" placeholder="tìm kiếm tại đây" />
          </div>

          <div className="PriceFilterWrapper flex text-black items-center">
            Giá từ
            <InputNumber
              placeholder="min"
              onChange={v => setMinPrice(v)}
              value={minPrice}
              min={0}
              max={1_000_000_000}
              style={{ margin: '0 16px', width: '120px' }}
            />
            -
            <InputNumber
              placeholder="max"
              onChange={v => setMaxPrice(v)}
              value={maxPrice}
              min={0}
              max={1_000_000_000}
              style={{ margin: '0 16px', width: '120px' }}
            />
          </div>

          <div className="SortWrapper flex flex-col w-[240px] border-1 border-gray-400 p-2">
            Sắp xếp:
            <div className='flex gap-2'>
              <div className='flex gap-2 items-center'>
                <label htmlFor='sortAlpha'>Theo chữ cái</label>
                <Switch size='small' id='sortAlpha' defaultChecked={alphabetSort} onChange={checked=>setAlphabetSort(checked)}/>
              </div>

              <div className='flex gap-2 items-center'>
                <label htmlFor='sortPrice'>Theo giá tiền</label>
                <Switch size='small' id='sortPrice' defaultChecked={priceSort} onChange={checked=>setPriceSort(checked)} />
              </div>
            </div>
          </div>
        </div>

        {
          courses && courses?.length > 0 && (<CourseList loading={loading} courses={courses} />) || (
            <h1 className='text-black'>No available items :(</h1>
          )
        }

        <div className="flex justify-center">
          <Pagination simple defaultCurrent={0} total={totalItems} showSizeChanger onChange={handlePagination} />
        </div>
      </div>


    </>
  )
}