'use strict';
 
app.factory('Connector', ['$http', '$q', function($http, $q){

    var server = "http://localhost:8080/";
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

        getUserDefaultDataTable: function() {
            return $http.get(server + 'getUserDefaultDataTable')
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

        getGeneralDataTable: function(chartSelection) {
            return $http.post(server + 'general/getGeneralDataTable', chartSelection)
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
            return $http.get('http://localhost:8080/ProjectJava11a/getUserChartSelectionsList')
                .then(
                    function(response){
                        return response.data.data;
                    },
                    function(errResponse){
                        console.error('Error while fetching users');
                        return $q.reject(errResponse);
                    }
                );
        },

        getPurposesList: function() {
            return $http.get('http://localhost:8080/ProjectJava11a/getPurposesList')
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

        getUserDataTable: function(chartSelection) {
            return $http.post('http://localhost:8080/ProjectJava11a/getUserDataTable', chartSelection)
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

        addCustomPurpose: function(purpose) {
            return $http.post('http://localhost:8080/ProjectJava11a/addCustomPurpose', purpose)
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

        addRecord: function(record) {
            return $http.post('http://localhost:8080/ProjectJava11a/addRecord', record)
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

        login: function(credentials) {
            return $http.post('http://localhost:8080/login', credentials)
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