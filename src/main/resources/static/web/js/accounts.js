const { createApp } = Vue;

createApp({
    data() {
        return {
            clientInfo: {},
            showCreateAcc: null,
            errorToats: null,
            errorMsg: null,
        }
    },
    methods: {
        getData() {
            axios.get("/api/clients/current")
                .then((response) => {
                    //get client ifo
                    this.clientInfo = response.data;
                    numAccounts = this.clientInfo.accounts.length;
                    this.showCreateAcc = numAccounts != 3 ? true : false;
                })
                .catch((error) => {
                    // handle error
                    this.errorMsg = "Error getting data";
                    this.errorToats.show();
                })
        },
        newAccount(){
            axios.post("/api/clients/current/accounts")
                .then((response) => {
                    //create new Account for client
                    console.log(response.data);
                    this.getData();
                })
                .catch((err) => {
                    // handle error
                    console.log(err);
                    this.errorMsg = "Cannot create Account";
                    this.errorToats.show();
                })
        },
        formatDate(date) {
            return new Date(date).toLocaleDateString('en-gb');
        },
        logout (){
            axios.post("/api/logout")
                .then(response => console.log('signed out.'))
                    .then
                return (window.location.href = "/web/index.html")
        }

    },
    mounted() {
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
    }
}).mount('#app');