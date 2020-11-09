function Quadrat() {
    var Eingabe  = document.getElementsByName("name");
    var Ergebnis = Eingabe.value;
    alert("Das Quadrat von " + Eingabe.value + " = " + Ergebnis);
    Eingabe.value = "Aaron";
   }