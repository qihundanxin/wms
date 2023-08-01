import axios from 'axios'

const downFile = (data)=>{
    return axios.request({
        url:data.url,
        method:'get',
        responseType:'blob'
    })
}

export default{
    downFile
}
