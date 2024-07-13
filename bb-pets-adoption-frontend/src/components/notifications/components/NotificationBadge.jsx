import React, { useEffect, useState, useContext } from 'react';
// import SockJS from 'sockjs-client';
// import Stomp from 'stompjs';
import { AuthContext } from '../../../context/AuthContext';
import instance from '../../../scripts/axiosConfig';
import { NotificationsContext } from '../../../context/NotificationsContext';
import { Container } from 'react-bootstrap';
import { useParams } from 'react-router-dom';


/****
 * 
 * 
 */
const NotificationBadge = ({ text }) => {

    const { setErrorMessage, notificationsCounter, setMyAppNotifications, setAppStatusNotifications, setNotificationMessage } = useContext(NotificationsContext);

    // useEffect(() => {
    //     if (user) {
    //         const socket = new SockJS('http://localhost:8080/ws');
    //         const stompClient = Stomp.over(socket);

    //         stompClient.connect({}, () => {
    //             stompClient.subscribe(`/topic/notifications/${user.id}`, (message) => {
    //                 const notification = JSON.parse(message.body);
    //                 setNotifications((prev) => [...prev, notification]);
    //             });
    //         });

    //         return () => {
    //             if (stompClient) {
    //                 stompClient.disconnect();
    //             }
    //         };
    //     }
    // }, [user]);

    const handleMarkAsRead = async (notificationId) => {
        try {
            await instance.post('/notifications/markAsRead', { notificationId });
            //  setNotifications((prev) => prev.filter((n) => n.id !== notificationId));
        } catch (error) {
            console.error('Error marking notification as read:', error);
        }
    };

    return (

        <Container id="notification_badge_wrapper">
            <Container id="notification_badge_container">
                <button onClick={() => handleMarkAsRead()}>{text}</button>
            </Container>

        </Container>

    );
};

export default NotificationBadge;
