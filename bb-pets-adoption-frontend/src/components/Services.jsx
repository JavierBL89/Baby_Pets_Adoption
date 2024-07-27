import React from "react";
import { Container, Row } from "react-bootstrap";
import Heading from "./common/Heading";
import TextComponent from "./common/TextComponent";






const Services = () => {


    return (

        <Container id="services_wrapper">
            <Container id="services_container" >
                <Row>
                    <Heading tagName="h1" text="Our Services" />

                    <TextComponent text="At  Bb Pet Adoption, we offer a variety of services to ensure that both pets and their new owners have the best possible experience. Our goal is to make pet adoption straightforward, enjoyable, and supportive every step of the way." />

                    <Heading tagName="h4" text="Pet Adoption" />
                    <TextComponent text="Founded in 2024, Bb Pet Adoption started as a small initiative by a group of passionate animal lovers. We saw the need for a reliable platform where people could find pets in need of a home and connect with trustworthy pet providers. Over the years, we've grown into a community that supports pet adoption and promotes responsible pet ownership." />

                    <Heading tagName="h4" text="What We Offer" />
                    <TextComponent text="Wide Selection of Pets: Whether you're looking for a playful puppy, a cuddly kitten, or a more mature companion, we have a variety of pets waiting for their forever homes." />
                    <ul>
                        <li><spam className="li_item">Browse Listings</spam>: Explore our extensive database of pets looking for a new home. Use our search filters to find the perfect match based on breed, age, size, and location.</li>

                        <li><spam className="li_item">Detailed Profiles</spam>:Each pet listing includes comprehensive information about the pet’s background, health, and personality to help you make an informed decision.

                        </li>
                        <li><spam className="li_item">Easy Application Process</spam>: Our streamlined application process makes it simple to express your interest in adopting a pet.
                        </li>
                    </ul>

                    <Heading tagName="h4" text="Adoption Support" />
                    <ul>
                        <li><spam className="li_item">Meet and Greet</spam>: Schedule meetings with pets and their providers to ensure a good match before finalizing the adoption.
                        </li>
                        <li><spam className="li_item">Guidance and Advice</spam>: Reach out to pet providers to learn more about the pets you're interested in.
                        </li>
                    </ul>

                    <Heading tagName="h4" text="Pet Provider Services" />
                    <ul>
                        <li><spam className="li_item">List Pets for Adoption</spam>: We provide detailed information about each pet, including their background, health, and temperament.
                        </li>
                        <li><spam className="li_item">Manage Inquiries</spam>: Join a supportive community of pet lovers and get advice and tips from experienced pet owners.
                        </li>
                        <li><spam className="li_item">Trusted Community</spam>: We prioritize the well-being of animals and work towards ensuring that every pet finds a safe and loving home.
                        </li>

                    </ul>

                    <Heading tagName="h4" text="Community Resources" />
                    <ul>
                        <li><spam className="li_item">Educational Articles</spam>: We provide detailed information about each pet, including their background, health, and temperament.
                        </li>
                        <li><spam className="li_item">Support Groups</spam>: Join a supportive community of pet lovers and get advice and tips from experienced pet owners.
                        </li>
                    </ul>

                    <Heading tagName="h4" text="Why Choose Us?" />
                    <ul>
                        <li><spam className="li_item">Transparency</spam>: We provide detailed information about each pet, including their background, health, and temperament.
                        </li>
                        <li><spam className="li_item">Community</spam>: Join a supportive community of pet lovers and get advice and tips from experienced pet owners.
                        </li>
                        <li><spam className="li_item">Commitment to Welfare</spam>: We prioritize the well-being of animals and work towards ensuring that every pet finds a safe and loving home.
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


export default Services;