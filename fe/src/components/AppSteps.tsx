import { Steps } from "antd"

type AppStepProps = {
    currentStep: number,
}

export const AppSteps: React.FC<AppStepProps> = ( {currentStep} ) => {
    return (
        <>
            <Steps
                className="flex pt-5 pb-12 px-24"
                current = {currentStep}
                items={[
                    {
                        title: 'Cart',
                    },
                    {
                        title: 'Checkout',
                    },
                    {
                        title: 'Oder',
                    },
                ]}
            />
        </>
    )
}