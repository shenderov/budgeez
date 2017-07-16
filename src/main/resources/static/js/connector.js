'use strict';

app.factory('Connector', ['$http', '$q', function ($http, $q) {
    return {
        getVersion: function () {
            return $http.get(serverPath + 'general/getVersion')
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        },

        getLanguages: function () {
            return $http.get(serverPath + 'general/getLanguages')
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        },

        getCurrencies: function () {
            return $http.get(serverPath + 'general/getCurrencies')
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        },

        getGeneralSelectionsList: function () {
            return $http.get(serverPath + 'general/getGeneralChartSelectionsList')
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        },

        getGeneralDefaultDataTable: function () {
            return $http.get(serverPath + 'general/getDefaultDataTable')
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        },

        getGeneralDataTable: function (chartRequestWrapper) {
            return $http.post(serverPath + 'general/getGeneralDataTable', chartRequestWrapper)
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        },

        getUserSelectionsList: function () {
            return $http.get(serverPath + 'general/getUserChartSelectionsList')
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        },

        getCategoriesList: function (token) {
            return $http.get(serverPath + 'user/getCategoriesList', {headers: token})
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        },

        getUserDefaultDataTable: function (token) {
            return $http.get(serverPath + 'user/getUserDefaultDataTable', {headers: token})
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        },

        getUserDataTable: function (chartRequestWrapper, token) {
            return $http.post(serverPath + 'user/getUserDataTable', chartRequestWrapper, {headers: token})
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        },

        addCustomCategory: function (category, token) {
            return $http.post(serverPath + 'user/addCustomCategory', category, {headers: token})
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        },

        addRecord: function (record, token) {
            return $http.post(serverPath + 'user/addRecord', record, {headers: token})
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        },

        getRecordsList: function (datePicker, token) {
            return $http.post(serverPath + 'user/getRecordsList', datePicker, {headers: token})
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        },

        deleteRecord: function (recordId, token) {
            return $http.post(serverPath + 'user/deleteRecord', recordId, {headers: token})
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        },

        editRecord: function (record, token) {
            return $http.post(serverPath + 'user/editRecord', record, {headers: token})
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        },

        getUserDetails: function (token) {
            return $http.get(serverPath + 'getUserDetails', {headers: token})
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        },

        updateUserDetails: function (userDetails, token) {
            return $http.post(serverPath + 'updateUserDetails', userDetails, {headers: token})
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        },

        login: function (credentials) {
            return $http.post(serverPath + 'login', credentials)
                .then(
                    function (result) {
                        return result.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        },

        signup: function (userDetails) {
            return $http.post(serverPath + 'signup', userDetails)
                .then(
                    function (result) {
                        return result.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        },

        reviveUser: function (credentials) {
            return $http.post(serverPath + 'reviveUser', credentials)
                .then(
                    function (result) {
                        return result.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        },

        isEmailRegistered: function (email) {
            return $http.post(serverPath + 'isEmailRegistered', email)
                .then(
                    function (result) {
                        return result.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        },

        refreshToken: function (token) {
            return $http.get(serverPath + 'refresh', {headers: token})
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        },

        changeEmail: function (changeEmailWrapper, token) {
            return $http.post(serverPath + 'changeEmail', changeEmailWrapper, {headers: token})
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        },

        changePassword: function (changePasswordWrapper, token) {
            return $http.post(serverPath + 'changePassword', changePasswordWrapper, {headers: token})
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        },

        deleteAccount: function (credentials, token) {
            return $http.post(serverPath + 'deleteAccount', credentials, {headers: token})
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        },

        confirm: function (token) {
            return $http.get(serverPath + 'confirm', {params:{token:token}})
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        },

        verify: function (token) {
            return $http.get(serverPath + 'verify', {params:{token:token}})
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse.data);
                    }
                );
        }
    };
}]);