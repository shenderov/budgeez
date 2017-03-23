'use strict';

app.factory('Connector', ['$http', '$q', function ($http, $q) {
    console.log("Connector");
    return {
        getGeneralSelectionsList: function () {
            return $http.get(serverPath + 'general/getGeneralChartSelectionsList')
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse);
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
                        return $q.reject(errResponse);
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
                        return $q.reject(errResponse);
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
                        return $q.reject(errResponse);
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
                        return $q.reject(errResponse);
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
                        return $q.reject(errResponse);
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
                        return $q.reject(errResponse);
                    }
                );
        },

        addCustomCategory: function (category, token) {
            return $http.post(serverPath + 'user/addCustomCategory', category, {headers: token})
                .then(
                    function (response) {
                        return response;
                    },
                    function (errResponse) {
                        return $q.reject(errResponse);
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
                        return $q.reject(errResponse);
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
                        return $q.reject(errResponse);
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
                        return $q.reject(errResponse);
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
                        return $q.reject(errResponse);
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
                        return $q.reject(errResponse);
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
                        return $q.reject(errResponse);
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
                        return $q.reject(errResponse);
                    }
                );
        }
    };
}]);