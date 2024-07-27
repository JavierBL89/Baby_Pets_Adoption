import React from "react";
import { Container, Row } from "react-bootstrap";
import Heading from "./common/Heading";
import TextComponent from "./common/TextComponent";






const About = () => {


    return (

        <Container id="about_wrapper">
            <Container id="about_container" >
                <Row>
                    <Heading tagName="h1" text="Welcome to Baby Pet Adoption!" />

                    <TextComponent text="At Baby Pet Adoption, we believe that every pet deserves a loving home and every person deserves the joy of a furry companion. Our mission is to make pet adoption easy, transparent, and enjoyable for everyone involved." />

                    <Heading tagName="h4" text="Our Story" />
                    <TextComponent text="Founded in 2024, Bb Pet Adoption started as a small initiative by a group of passionate animal lovers. We saw the need for a reliable platform where people could find pets in need of a home and connect with trustworthy pet providers. Over the years, we've grown into a community that supports pet adoption and promotes responsible pet ownership." />


                    <Heading tagName="h4" text="Our Adoption Process" />
                    <ul>
                        <li><spam className="li_item">Browse Pets</spam>: Explore our listings to find the perfect pet that fits your lifestyle and preferences.
                        </li>
                        <li><spam className="li_item">Connect with Providers</spam>: Reach out to pet providers to learn more about the pets you're interested in.
                        </li>
                        <li><spam className="li_item">Submit an Application</spam>: Fill out a simple adoption application to express your interest.
                        </li>
                        <li><spam className="li_item">Meet and Greet</spam>: Arrange a meeting with the pet to see if it's a good match.
                        </li>
                        <li><spam className="li_item">Bring Home Your Pet</spam>: Complete the adoption process and welcome your new family mesmber home!
                        </li>

                    </ul>
                    <Heading tagName="h4" text="Join Us in Making a Difference" />
                    <TextComponent text="Adopting a pet is a rewarding experience that brings joy and companionship into your life. By choosing to adopt, you're also helping to reduce the number of homeless pets and supporting a compassionate community." />
                    <TextComponent text="Thank you for considering [Your Service Name] for your pet adoption journey. We look forward to helping you find your perfect pet match!" />
                </Row>
            </Container>

        </Container>
    )
}


export default About;