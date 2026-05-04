import React, { useEffect, useRef } from 'react';

declare global {
    interface Window {
        turnstile: {
            render: (container: string | HTMLElement, options: unknown) => string;
            remove: (widgetId: string) => void;
            reset: (widgetId: string) => void;
        };
    }
}

interface TurnstileProps {
    sitekey: string;
    onVerify: (token: string) => void;
    className?: string;
    isActive?: boolean;
}

const Turnstile: React.FC<TurnstileProps> = ({ sitekey, onVerify, className, isActive }) => {
    const containerRef = useRef<HTMLDivElement>(null);
    const widgetIdRef = useRef<string | null>(null);

    useEffect(() => {
        if (isActive) {
            if (containerRef.current && window.turnstile) {
                if (widgetIdRef.current) {
                    window.turnstile.reset(widgetIdRef.current);
                } else {
                    widgetIdRef.current = window.turnstile.render(containerRef.current, {
                        sitekey,
                        callback: onVerify,
                    });
                }
            }
        }

        return () => {
            if (widgetIdRef.current && window.turnstile) {
                window?.turnstile.remove(widgetIdRef.current);
                widgetIdRef.current = null;
            }
        };
    }, [isActive]);

    return <div ref={containerRef} className={className} />;
};

const memoizedTurnstile = React.memo(Turnstile);

export { memoizedTurnstile as Turnstile };
