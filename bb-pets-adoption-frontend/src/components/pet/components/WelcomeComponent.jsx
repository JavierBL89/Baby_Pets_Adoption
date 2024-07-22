import React, { useContext, useState } from "react";
import { Container, Row } from "react-bootstrap";
import { AuthContext } from '../../../context/AuthContext';
import Heading from "../../common/Heading";
import { NotificationsContext } from "../../../context/NotificationsContext";
import NotificationMessageComponent from "../../notifications/components/NotificationMessageComponent";
import useFetchNotifications from "../../hooks/data/fetchNotifications";


/***
 * Component for holding homepage welcome message.
 * 
 * Additionaly uses authentication app state to display or hide a sign in message 
 * 
 */
const WelcomeComponent = () => {

    const { isAuthenticated } = useContext(AuthContext);
    const { notificationsMessage } = useContext(NotificationsContext);

    useFetchNotifications();

    return (
        <Container >
            <Container >
                <Row id="notifications_space">
                    {!isAuthenticated ?
                        (
                            <Container id="sign_in_holder">
                                <Heading tagName="h6" id="sign_in_messsage" text="Sign in to access adoption services !" />
                            </Container>
                        )
                        : notificationsMessage ?
                            (
                                <NotificationMessageComponent />
                            )
                            :
                            null
                    }
                </Row>
            </Container>
        </Container>
    )

};


export default WelcomeComponent;