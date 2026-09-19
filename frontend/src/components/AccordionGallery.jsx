import { useMemo, useRef, useState } from "react";
import { Link } from "react-router-dom";
import "./AccordionGallery.css";

const EASE_MAP = {
    linear: "linear",
    "power1.out": "cubic-bezier(0.11, 0, 0.5, 0)",
    "power1.inOut": "cubic-bezier(0.45, 0, 0.55, 1)",
    "power2.out": "cubic-bezier(0.16, 0.84, 0.44, 1)",
    "power2.inOut": "cubic-bezier(0.65, 0, 0.35, 1)",
    "power3.out": "cubic-bezier(0.16, 1, 0.3, 1)",
    "power3.inOut": "cubic-bezier(0.76, 0, 0.24, 1)",
    "power4.out": "cubic-bezier(0.11, 1, 0.24, 1)",
    "back.out": "cubic-bezier(0.34, 1.56, 0.64, 1)",
    "elastic.out": "cubic-bezier(0.68, -0.55, 0.27, 1.55)",
};

function resolveEase(ease) {
    return EASE_MAP[ease] || ease || "cubic-bezier(0.16, 1, 0.3, 1)";
}

function AccordionPanel({
                            item,
                            index,
                            isActive,
                            isHorizontal,
                            activeSize,
                            restSize,
                            duration,
                            ease,
                            stagger,
                            radius,
                            grayscale,
                            showLabels,
                            accentColor,
                            overlayColor,
                            textColor,
                            parallax,
                            tilt,
                            onActivate,
                            onClickActivate,
                        }) {
    const panelRef = useRef(null);
    const [pointer, setPointer] = useState({ x: 0, y: 0 });

    function handleMouseMove(e) {
        if (!isActive || (!parallax && !tilt)) return;
        const rect = panelRef.current.getBoundingClientRect();
        const x = (e.clientX - rect.left) / rect.width - 0.5; // -0.5..0.5
        const y = (e.clientY - rect.top) / rect.height - 0.5;
        setPointer({ x, y });
    }

    function handleMouseLeave() {
        setPointer({ x: 0, y: 0 });
    }

    const imageTransform = [
        `scale(${isActive ? 1.12 : 1.02})`,
        parallax ? `translate(${pointer.x * -40 * parallax}px, ${pointer.y * -40 * parallax}px)` : "",
        tilt ? `rotateY(${pointer.x * tilt}deg) rotateX(${pointer.y * -tilt}deg)` : "",
    ]
        .filter(Boolean)
        .join(" ");

    const easeCss = resolveEase(ease);
    const sizeProp = isHorizontal ? "flexBasis" : "flexBasis";

    return (
        <div
            ref={panelRef}
            className={`accordion-panel${isActive ? " is-active" : ""}`}
            style={{
                [sizeProp]: `${isActive ? activeSize : restSize}%`,
                borderRadius: `${radius}px`,
                transitionDuration: `${duration}s`,
                transitionTimingFunction: easeCss,
                transitionDelay: `${index * stagger}s`,
            }}
            onMouseEnter={onActivate}
            onMouseMove={handleMouseMove}
            onMouseLeave={handleMouseLeave}
            onClick={onClickActivate}
        >
            <div
                className="accordion-panel-image"
                style={{
                    transform: imageTransform,
                    transitionDuration: `${duration}s`,
                    transitionTimingFunction: easeCss,
                    filter: grayscale && !isActive ? "grayscale(1) brightness(0.85)" : "none",
                }}
            >
                <img src={item.image} alt={item.label || ""} draggable={false} />
            </div>

            <div
                className="accordion-panel-overlay"
                style={{
                    background: `linear-gradient(to top, ${overlayColor}dd 0%, ${overlayColor}66 40%, transparent 75%)`,
                    opacity: isActive ? 1 : 0.55,
                    transitionDuration: `${duration}s`,
                }}
            />

            {showLabels && (
                <div
                    className="accordion-panel-content"
                    style={{
                        opacity: isActive ? 1 : 0,
                        transform: `translateY(${isActive ? 0 : 10}px)`,
                        transitionDuration: `${duration * 0.8}s`,
                        transitionTimingFunction: easeCss,
                    }}
                >
                    <span
                        className="accordion-panel-accent"
                        style={{ background: accentColor }}
                    />

                    {item.link ? (
                        <Link
                            to={item.link}
                            className="accordion-panel-label"
                            style={{ color: textColor }}
                        >
                            {item.label}
                        </Link>
                    ) : (
                        <span
                            className="accordion-panel-label"
                            style={{ color: textColor }}
                        >
                            {item.label}
                        </span>
                    )}

                    {item.desc && (
                        <p
                            className="accordion-panel-desc"
                            style={{ color: textColor }}
                        >
                            {item.desc}
                        </p>
                    )}

                    {item.subLabel && item.subLink && (
                        <Link
                            to={item.subLink}
                            className="accordion-panel-sublink"
                            style={{ color: textColor, borderColor: `${textColor}55` }}
                            onClick={(e) => e.stopPropagation()}
                        >
                            {item.subLabel}
                        </Link>
                    )}
                </div>
            )}

            {!isActive && showLabels && (
                <div
                    className="accordion-panel-collapsed-label"
                    style={{ color: textColor }}
                >
                    <span>{item.label}</span>
                </div>
            )}
        </div>
    );
}

export default function AccordionGallery({
                                             items = [],
                                             defaultIndex = 0,
                                             expandRatio = 0.5,
                                             trigger = "hover",
                                             accentColor = "#ffffff",
                                             overlayColor = "#060010",
                                             textColor = "#ffffff",
                                             grayscale = false,
                                             showLabels = true,
                                             duration = 0.6,
                                             ease = "power3.out",
                                             parallax = 0,
                                             tilt = 0,
                                             stagger = 0,
                                             height = 420,
                                             gap = 10,
                                             radius = 16,
                                             orientation = "horizontal",
                                         }) {
    const [activeIndex, setActiveIndex] = useState(defaultIndex);
    const isHorizontal = orientation === "horizontal";

    const { activeSize, restSize } = useMemo(() => {
        const activePct = Math.min(Math.max(expandRatio, 0.1), 0.9) * 100;
        const remaining = 100 - activePct;
        const restPct = items.length > 1 ? remaining / (items.length - 1) : remaining;
        return { activeSize: activePct, restSize: restPct };
    }, [expandRatio, items.length]);

    return (
        <div
            className={`accordion-gallery ${isHorizontal ? "is-horizontal" : "is-vertical"}`}
            style={{
                [isHorizontal ? "height" : "width"]: `${height}px`,
                gap: `${gap}px`,
            }}
            onMouseLeave={() => {
                if (trigger === "hover") setActiveIndex(defaultIndex);
            }}
        >
            {items.map((item, index) => (
                <AccordionPanel
                    key={item.label ?? index}
                    item={item}
                    index={index}
                    isActive={index === activeIndex}
                    isHorizontal={isHorizontal}
                    activeSize={activeSize}
                    restSize={restSize}
                    duration={duration}
                    ease={ease}
                    stagger={stagger}
                    radius={radius}
                    grayscale={grayscale}
                    showLabels={showLabels}
                    accentColor={accentColor}
                    overlayColor={overlayColor}
                    textColor={textColor}
                    parallax={parallax}
                    tilt={tilt}
                    onActivate={() => {
                        if (trigger === "hover") setActiveIndex(index);
                    }}
                    onClickActivate={() => {
                        if (trigger === "click") setActiveIndex(index);
                    }}
                />
            ))}
        </div>
    );
}
