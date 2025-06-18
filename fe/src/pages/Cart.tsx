import { AppSteps, CartList } from "@components"

export const Cart: React.FC = () => {
    return (
        <>
            <AppSteps currentStep={0} />
            <div className='Cart flex flex-col items-center px-24 gap-2'>
                <CartList />
            </div>
        </>
    )
}