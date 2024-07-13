import React, { useContext, useState, useEffect, useCallback } from "react";
import instance from "../../../scripts/axiosConfig";
import { useParams } from "react-router-dom";
import { AuthContext } from "../../../context/AuthContext";
import { NotificationsContext } from "../../../context/NotificationsContext";



/**
 * Custom Hook makes API GET request to retrieve notifications.
 *
 * This hook listens to changes on the authentication state to make a GET request for notifications
 *
 * @returns notifications and other useful states to be used across other modules
 */
const useFetchNotifications = () => {

    const { setUnviewedListingsNotifications, setErrorMessage,
        setUnviewedAppStatusNotifications, setNotificationsMessage } = useContext(NotificationsContext);

    // const {  } = useContext(AuthContext);

    const { token } = useParams();

    const fetchNotifications = useCallback(async () => {

        // check if authenticated or token is missing 
        if (!token) {
            console.error("User not autthenticated or token is missing");
            return;
        }

        if (token) {
            try {
                const response = await instance.get(`/notifications/pending?${token}`);
                if (response.status === 200) {

                    const result = response.data;
                    if (result) {
                        const unviewedListingspNotifications = result.unviewedListingspNotifications || [];
                        const unviewedAppStatusNotifications = result.unviewedAppStatusNotifications || [];
                        setUnviewedAppStatusNotifications(unviewedAppStatusNotifications);
                        setUnviewedListingsNotifications(unviewedListingspNotifications);
                        setNotificationsMessage("You have " +
                            (unviewedListingspNotifications.length + unviewedAppStatusNotifications.length)
                            + " new notifications")
                    }

                }
            } catch (error) {
                console.error('Error fetching notifications:', error);
            }
        } else {
            console.error("Authentication needed. Form could not be submited!")
            setErrorMessage("Authentication needed. Notification could no be retrieved")
        }


    }, [token, setErrorMessage, setUnviewedListingsNotifications,
        setUnviewedAppStatusNotifications, setNotificationsMessage]);


    /***
    * useEfect listens to changes on token from Auth context 
    * to get triggered and perform a get request to API and handle response.
    * 
    * When the user first login, 'pending' notifications will be retreived from db
    * and set the 'notifications' state of NotificationsContext with them. 
    * */
    useEffect(() => {
        fetchNotifications();
    }, [token, fetchNotifications]);

    return {};

};



export default useFetchNotifications;