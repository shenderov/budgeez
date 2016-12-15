'use strict';
 
app.factory('Connector', ['$http', '$q', function($http, $q){

    var server = "/kamabizbazti/";

    return {
         
    getGeneralSelectionsList: function() {
            return $http.get(server + 'general/getGeneralChartSelectionsList')
            .then(
                    function(response){
                        return response.data;
                    }, 
                    function(errResponse){
                        console.error('Error while fetching users');
                        return $q.reject(errResponse);
                    }
            );
        },

        getGeneralDefaultDataTable: function() {
            return $http.get(server + 'general/getDefaultDataTable')
                .then(
                    function(response){
                        return response.data;
                    },
                    function(errResponse){
                        console.error('Error while fetching users');
                        return $q.reject(errResponse);
                    }
                );
        },

        getUserDefaultDataTable: function(token) {
            return $http.get(server + 'user/getUserDefaultDataTable', {headers: token})
                .then(
                    function(response){
                        return response.data;
                    },
                    function(errResponse){
                        console.error('Error while fetching users');
                        return $q.reject(errResponse);
                    }
                );
        },

        getGeneralDataTable: function(chartRequestWrapper) {
            return $http.post(server + 'general/getGeneralDataTable', chartRequestWrapper)
                .then(
                    function(response){
                        return response.data;
                    },
                    function(errResponse){
                        console.error('Error while fetching users');
                        return $q.reject(errResponse);
                    }
                );
        },

        getUserSelectionsList: function() {
            return $http.get(server + 'general/getUserChartSelectionsList')
                .then(
                    function(response){
                        return response.data;
                    },
                    function(errResponse){
                        console.error('Error while fetching users');
                        return $q.reject(errResponse);
                    }
                );
        },

        getPurposesList: function(token) {
            return $http.get(server + 'user/getPurposesList', {headers: token})
                .then(
                    function(response){
                        return response.data;
                    },
                    function(errResponse){
                        console.error('Error while fetching users');
                        return $q.reject(errResponse);
                    }
                );
        },

        getUserDataTable: function(chartRequestWrapper, token) {
            return $http.post(server + 'user/getUserDataTable', chartRequestWrapper, {headers: token})
                .then(
                    function(response){
                        return response.data;
                    },
                    function(errResponse){
                        console.error('Error while fetching users');
                        return $q.reject(errResponse);
                    }
                );
        },

        addCustomPurpose: function(purpose, token) {
            return $http.post(server + 'user/addCustomPurpose', purpose, {headers: token})
                .then(
                    function(response){
                        return response;
                    },
                    function(errResponse){
                        console.error('Error while fetching users');
                        return $q.reject(errResponse);
                    }
                );
        },

        addRecord: function(record, token) {
            return $http.post(server + 'user/addRecord', record, {headers: token})
                .then(
                    function(response){
                        return response.data;
                    },
                    function(errResponse){
                        console.error('Error while fetching users');
                        return $q.reject(errResponse);
                    }
                );
        },

        getRecordsList: function(datePicker, token) {
            return $http.post(server + 'user/getRecordsList', datePicker, {headers: token})
                .then(
                    function(response){
                        return response.data;
                    },
                    function(errResponse){
                        console.error('Error while fetching users');
                        return $q.reject(errResponse);
                    }
                );
        },

        deleteRecord: function(recordId, token) {
            return $http.post(server + 'user/deleteRecord', recordId, {headers: token})
                .then(
                    function(response){
                        return response.data;
                    },
                    function(errResponse){
                        console.error('Error while fetching users');
                        return $q.reject(errResponse);
                    }
                );
        },

        editRecord: function(record, token) {
            return $http.post(server + 'user/editRecord', record, {headers: token})
                .then(
                    function(response){
                        return response.data;
                    },
                    function(errResponse){
                        console.error('Error while fetching users');
                        console.error(JSON.stringify(errResponse));
                        return $q.reject(errResponse);
                    }
                );
        },

        login: function(credentials) {
            return $http.post(server + 'login', credentials)
                .then(
                    function(result){
                        return result.data;
                    },
                    function(errResponse){
                        console.error('Error while fetching users');
                        return $q.reject(errResponse);
                    }
                );
        },

        signup: function(userDetails) {
            return $http.post(server + 'signup', userDetails)
                .then(
                    function(result){
                        return result.data;
                    },
                    function(errResponse){
                        console.error('Error while fetching users');
                        return $q.reject(errResponse);
                    }
                );
        },


     
   // createUser: function(user){
   //         return $http.post('http://localhost:8080/SpringMVC4RestAPI/user/', user)
   //         .then(
   //                 function(response){
   //                     return response.data;
   //                 },
   //                 function(errResponse){
   //                     console.error('Error while creating user');
   //                     return $q.reject(errResponse);
   //                 }
   //         );
   //     },
   //
   // updateUser: function(user, id){
   //         return $http.put('http://localhost:8080/SpringMVC4RestAPI/user/'+id, user)
   //         .then(
   //                 function(response){
   //                     return response.data;
   //                 },
   //                 function(errResponse){
   //                     console.error('Error while updating user');
   //                     return $q.reject(errResponse);
   //                 }
   //         );
   //     },
   //
   //deleteUser: function(id){
   //         return $http.delete('http://localhost:8080/SpringMVC4RestAPI/user/'+id)
   //         .then(
   //                 function(response){
   //                     return response.data;
   //                 },
   //                 function(errResponse){
   //                     console.error('Error while deleting user');
   //                     return $q.reject(errResponse);
   //                 }
   //         );
   //     }
         
    };
 
}]);