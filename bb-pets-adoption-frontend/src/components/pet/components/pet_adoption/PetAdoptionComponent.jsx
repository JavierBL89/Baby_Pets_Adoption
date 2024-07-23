import React, { useCallback, useEffect, useState, useContext } from "react";
import { Container, Row } from "react-bootstrap";
import Heading from "../../../common/Heading";
import TextComponent from "../../../common/TextComponent";
import ButtonComponent from "../../../common/ButtonComponent";
import PetAdoptionForm from "./PetAdoptionForm";
import { AuthContext } from "../../../../context/AuthContext";


/***
 * Component acts as core for the addoption application section
 * 
 * It is responsibe for holding the form for a pet adoption, 
 * and any other components that could be added in future
 * 
 * @returns  'PetAdoptionComponent' - thr component
 */
const PetAdoptionComponent = ({ petId, userName }) => {

    const [viewForm, setViewForm] = useState(null);

    /**** 
     * Method toggles the view application dropdown.
     * 
     * Sets the state to the oposite of the current state is
     * false to true | true to false
    */
    const viewFormToggle = () => {
        setViewForm(!viewForm);
    }

    return (

        <Container id="pet_adoption_wrapper">
            <Container id="pet_adoption_container">
                <Row >
                    <Container id="bg_house_pet_view" >
                        <img src={process.env.PUBLIC_URL + "/assets/images/house_and_foot_prints.png"} alt="background" />

                    </Container>
                    <Heading id="adoption_form_title" tagName="h4" text="Adoption Applications" />
                </Row>
                <Row >
                    <TextComponent id="before_application_text"
                        text="Before you apply....\n We'd like you to be sure of the responsabilities that come with introducing
                    a pet in yor life. \n Additionally we ask you ensure a correct communication with the pet provider,
                     and to make you aware that there could be applications before yours and pet provider could take so time to evaluate each application. 
                     In the meantime you can track your application status. We also notify you of any statues changes.\n "

                    />

                </Row>
                <Row className="application_adoption_holder">
                    {/******* Drop app button *******/}
                    <Row id="adoption_form_dropdown">
                        <ButtonComponent onClick={viewFormToggle} id="pet_adoption_dropdown_button" className="btn no-arrow "
                            text=" Adoption Request" />


                        {
                            viewForm &&
                            <Row id="adoption_form_body">
                                <PetAdoptionForm petId={petId} userName={userName} />
                            </Row>
                        }
                    </Row>
                </Row>

            </Container>

        </Container >
    );

};

export default PetAdoptionComponent;
