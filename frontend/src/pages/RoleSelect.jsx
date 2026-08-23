import { Navigate } from "react-router-dom";
import { session } from "../api";
import Layout from "../components/Layout";
import AccordionGallery from "../components/AccordionGallery";

import ngoImage from "../assets/Images/NGO.png";
import kitchenImage from "../assets/Images/kitchen_stuff.png";
import hotelImage from "../assets/Images/Hotel.jpg";

const roleItems = [
    {
        image: hotelImage,
        label: "Hotel Manager",
        desc: "Review surplus food, manage donations and coordinate NGO requests.",
        link: "/signup/hotel",
        subLabel: "Log in",
        subLink: "/login?type=HOTEL_MANAGER",
    },
    {
        image: kitchenImage,
        label: "Kitchen Staff",
        desc: "Quickly record safe surplus food directly from your kitchen.",
        link: "/signup/staff",
        subLabel: "Log in",
        subLink: "/login?type=KITCHEN_STAFF",
    },
    {
        image: ngoImage,
        label: "NGO",
        desc: "Discover available food and request donations for your community.",
        link: "/signup/ngo",
        subLabel: "Log in",
        subLink: "/login?type=NGO",
    },
];

export default function RoleSelect() {
    const user = session.get();

    if (user) {
        if (user.userType === "HOTEL_MANAGER") {
            return <Navigate to="/hotel" replace />;
        }

        if (user.userType === "KITCHEN_STAFF") {
            return <Navigate to="/kitchen" replace />;
        }

        if (user.userType === "NGO") {
            return <Navigate to="/ngo" replace />;
        }
    }

    return (
        <Layout>
            <div className="center-box">

                {/* =========================
                    HERO SECTION
                ========================= */}

                <div className="hero">
                    <h1>
                        Give surplus food a{" "}
                        <span>second serve</span>
                    </h1>

                    <p>
                        SecondServe connects hotels, kitchen teams and NGOs
                        so good food can reach people instead of going to waste.
                    </p>
                </div>


                {/* =========================
                    ROLE SELECTOR
                ========================= */}

                <div className="role-cards">
                    <AccordionGallery
                        items={roleItems}
                        defaultIndex={0}
                        expandRatio={0.52}
                        trigger="hover"
                        accentColor="#f0a35b"
                        overlayColor="#0b1a12"
                        textColor="#ffffff"
                        grayscale
                        showLabels
                        duration={0.6}
                        ease="power3.out"
                        parallax={0.5}
                        tilt={6}
                        stagger={0.06}
                        height={440}
                        gap={10}
                        radius={20}
                        orientation="horizontal"
                    />
                </div>


                {/* =========================
                    HOW IT WORKS
                ========================= */}

                <div className="how-it-works">

                    <div className="how-it-works-top">

                        <div>

                            <h2>
                                Good food shouldn't go to waste.
                            </h2>
                        </div>



                    </div>


                    {/* FLOW */}

                    <div className="journey">

                        {/* STEP 01 */}
                        <div className="journey-step step-green">

                            <div className="step-top">


                            </div>



                            <h3>
                                Hotel
                            </h3>

                            <p>
                                Safe surplus food is added to SecondServe
                                before it goes to waste.
                            </p>

                        </div>


                        {/* ARROW */}

                        <div className="journey-connector">
                            <div className="connector-line"></div>

                            <div className="connector-arrow">
                                ›
                            </div>
                        </div>


                        {/* STEP 02 */}

                        <div className="journey-step step-orange">

                            <div className="step-top">






                            </div>


                            <h3>
                                Surplus Food
                            </h3>

                            <p>
                                Available food is listed with its quantity,
                                location and collection details.
                            </p>

                        </div>


                        {/* ARROW */}

                        <div className="journey-connector">
                            <div className="connector-line"></div>

                            <div className="connector-arrow">
                                ›
                            </div>
                        </div>


                        {/* STEP 03 */}

                        <div className="journey-step step-green">


                            <h3>
                                NGO
                            </h3>

                            <p>
                                NGOs discover available donations and request
                                the food they need.
                            </p>

                        </div>


                        {/* ARROW */}

                        <div className="journey-connector">
                            <div className="connector-line"></div>

                            <div className="connector-arrow">
                                ›
                            </div>
                        </div>


                        {/* STEP 04 */}

                        <div className="journey-step step-dark">





                            <h3>
                                Community
                            </h3>

                            <p>
                                Good food gets a second chance and reaches
                                people who need it.
                            </p>

                        </div>

                    </div>



                </div>

            </div>
        </Layout>
    );
}