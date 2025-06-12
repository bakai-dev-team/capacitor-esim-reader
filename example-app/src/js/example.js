import { EsimReader } from 'capacitor-esim-reader';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    EsimReader.echo({ value: inputValue })
}
