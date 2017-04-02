'use strict';

app.factory('Connector', ['$http', '$q', function ($http, $q) {
    console.log("Connector");
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
        }
    };
}]);