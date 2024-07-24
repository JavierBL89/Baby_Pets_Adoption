import React from 'react';
import { Container } from 'react-bootstrap';
import TextComponent from "../../../components/common/TextComponent";

/****
 * 
 * 
 */
const NotificationBadge = ({ text }) => {

    return (

        <Container id="notification_badge_wrapper">
            <Container id="notification_badge_container">
                <TextComponent text={text} />
            </Container>

        </Container>

    );
};

export default NotificationBadge;
