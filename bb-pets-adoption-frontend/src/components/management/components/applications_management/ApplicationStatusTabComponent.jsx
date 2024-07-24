import React, { useState, useContext, } from "react";
import petTagsData from "../../../../petTagsData";
import { Container, Row, Col, Tab, Tabs, Button } from "react-bootstrap";
import { DataPetContext } from "../../../../context/DataPetContext";
import { IoIosRemoveCircle } from "react-icons/io";
import ButtonComponent from "../../../common/ButtonComponent";


/***
 * ApplicationStatusTabComponent Component
 * 
 * This component is responsible for managing and displaying the status tabs and passing the selected status back to the parent component
 * 
 * Props:
 *  - onTabSelect (function): Callback function to pass the selected tab name back to the parent component.
 * 
 * State:
 *  - statusesList (array): A list of statuses.
 *  - status (string): The currently selected status.
 * 
 * Usage:
 *  <ApplicationStatusTabComponent onTabSelect={handleTabSelection} />
 */
const ApplicationStatusTabComponent = ({ onTabSelect }) => {

    const [statusesList] = useState(["Pending", "Viewed", "Accepted", "Selected"]);
    const [status, setStatus] = useState("Pending");


    /***
    * Method handles status selection.
    * 
    * When user clicks on a status, this method updates the local state and 
    * calls the parent component's callback function to pass the selected status back
    * 
    * @params title - the status title
    */
    const handleStatus = (name) => {
        setStatus(name);
        onTabSelect(name); // Pass the selected status back to the parent
    };



    return (

        <Container id="statuses_tap_wrapper">
            <Container id="statuses_tap_container">
                <Row >
                    {
                        statusesList.map((status, statusIndex) => {
                            return (
                                <Col xs={6} lg={3} >
                                    <ButtonComponent
                                        key={statusIndex}
                                        text={status}
                                        id={`tab_${statusIndex}`}
                                        className={`tab_${status}`}
                                        eventKey={`${status}`}
                                        name={`${status}`}
                                        onClick={() => handleStatus(status)}
                                    >
                                        {/* Add any additional content you need inside each Tab */}
                                    </ButtonComponent>
                                </Col>
                            )
                        })
                    }
                </Row>
            </Container>
        </Container>
    );
};
export default ApplicationStatusTabComponent;