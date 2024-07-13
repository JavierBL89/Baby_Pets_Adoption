import React, { useContext, useEffect, useState } from "react";
import { Container } from "react-bootstrap";
import { FaCheck } from "react-icons/fa";
import { NotificationsContext } from "../../../context/NotificationsContext";
import Heading from "../../common/Heading";
import instance from "../../../scripts/axiosConfig";


/**
 * Component to display a information message when a new notification comes in.
 * It let's the user marking notifications as viewed through a PUT request
 * 
 * @param {String} text - a custom message to render whe needed
 * @param {String} token - the user autentication token
 * @param {String} notificationId - the id of the notification itself
 * @param {String} applicationId - the adoption application Id with notifcation is related to
 * @param {*} onViewed - a fucntion from parent component to pass up data when clicked 
 * @returns The `NotificationMessageComponent` functional component is being returned
 */
const NotificationMessageComponent = ({ text, token, notificationId, onFetchData }) => {

    const { notificationsMessage } = useContext(NotificationsContext);
    const [message, setMessage] = useState("")

    /**
        * Method to handle notifications as marked.
        * It makes a PUT request to update the notifiction status
        * @param {*} notificationId - the ID of the notification
        * @param {*} applicationId - the ID of the adoptuion application
        * @returns 
        */
    const handleViewed = async (notificationId) => {

        if (!notificationId || notificationId == null) {
            return
        }

        try {
            // PUT request
            const response = await instance.put(`/notifications/markAsViewed?token=${token}&notificationId=${notificationId}`);
            if (response.status === 200) {
                onFetchData();
            } else {
                console.error("Item could not be removed:", response.data);
                setMessage("A server error occured and pe could not be removed.Please try later or contact admin to inform about the problem.")
            }

        } catch (error) {
            console.error('Error deleting item:', error);
        }
    }

    /**
     * useEffect listens to when new message is set in 'notificationMessage'
     */
    useEffect(() => {
        setMessage([]);
        setMessage(notificationsMessage);
    }, [notificationsMessage]);


    return (
        <>  {/*********** diplays a message if 'notificationsMessage' data is found ******** */}
            {notificationsMessage &&
                < Container id="notification_message_holder" >
                    <Container id="notification_message_container">
                        <Heading tagName="h6" className="notification_message" text={message} />
                    </Container>
                </Container >

            }
            {/*********** diplays a message if 'text' data is passed ******** */}
            {text &&
                < Container id="notification_message_holder" >
                    <Container id="notification_message_container">
                        <Heading tagName="h6" className="notification_message" text={text} />
                        <FaCheck onClick={() => handleViewed(notificationId)} />
                    </Container>
                </Container >

            }
        </>
    )
};

export default NotificationMessageComponent;
