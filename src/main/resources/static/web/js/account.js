Vue.createApp({
    data() {
        return {
            accountInfo: {},
            errorToats: null,
            errorMsg: null,
        }
    },
    methods: {
        getData() {
            const urlParams = new URLSearchParams(window.location.search);
            const id = urlParams.get('id');
            //console.log("id= ",id);
            axios.get(`/api/current/accounts/${id}`)
                .then((response) => {
                    //get client ifo
                    this.accountInfo = response.data;
                    console.log(response.data);
                    this.accountInfo.transactions.sort((a, b) => b.id - a.id)
                })
                .catch((error) => {
                    // handle error
                    console.log(error);
                    this.errorMsg = "Error getting data";
                    this.errorToats.show();
                })
        },
        formatDate(date) {
            return new Date(date).toLocaleDateString('en-gb');
        }
    },
    mounted() {
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();
    }
}).mount('#app');