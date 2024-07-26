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
const DashBoardTabComponent = ({ onTabSelect }) => {

    const [tabOptions] = useState(["My details", "Email address"]);


    /***
    * Method handles status selection.
    * 
    * When user clicks on a status, this method updates the local state and 
    * calls the parent component's callback function to pass the selected status back
    * 
    * @params title - the status title
    */
    const handleTab = (name) => {
        onTabSelect(name); // Pass the selected status back to the parent
    };



    return (

        <Container id="statuses_tap_wrapper">
            <Container id="statuses_tap_container">
                <Row >
                    {
                        tabOptions.map((tabName, statusIndex) => {
                            return (
                                <Col xs={6}>
                                    <ButtonComponent
                                        key={statusIndex}
                                        text={tabName}
                                        id={`tab_${statusIndex}`}
                                        className={`tab_${tabName}`}
                                        eventKey={`${tabName}`}
                                        name={`${tabName}`}
                                        onClick={() => handleTab(tabName)}
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
export default DashBoardTabComponent;