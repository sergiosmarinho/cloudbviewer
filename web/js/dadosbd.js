//confs iniciais
$(document).ready(function(){
    $("#titulobd").html("Dados do banco "+dbname);
    escondebts();
});
$(document).ready(plotaGrafico);
var checkSlaRecente = false;
var largura = $(window).width();
var resizeEmAndamento = false;
var slaValue = null;
var pathConsulta = "bancoespecifico?db="+dbname;

$( window ).resize(function() {
    if(resizeEmAndamento==false && largura!=$(window).width()){
        resizeEmAndamento=true;
        plotaGrafico();
        largura=$(window).width();
        window.setTimeout(finalizaResize, 500);
    }
});

function finalizaCheckSlaRecente(){
    checkSlaRecente = false;
}

function finalizaResize(){
    resizeEmAndamento=false;
}

function alertaQuebraSLA(r,p){
    if(checkSlaRecente == false){
        checkSlaRecente=true;
        window.setTimeout(finalizaCheckSlaRecente, 45000);
        var posr = r.length - 1;
	var posp = p.length - 1;
	var sound = document.getElementById("audio");
	if(r[posr].real>slaValue){
            sound.play();
            alertaAviso("","O SLA foi violado!");
	}else if(p[posp].previsto>slaValue){
            sound.play();
            alertaAviso("","Há uma previsão de que o SLA será violado no próximo minuto!");
	}
    }
}

function getSla(r,p,sla){
	var ar=[];
	var escolhido;
	var aux={date:"",valor:""};
        //alert(r[r.length-1].date)
	if(r.length>p.length){
		escolhido=r;
	}else{
		escolhido=p;
	}
	for(i=0;i<escolhido.length;i++){
		if(escolhido[i].real!="null"){
			aux.date=escolhido[i].date;
			aux.valor=sla;
			ar.push(aux);
		}
		aux={date:"",real:""};
	}
        aux={date:p[p.length-1].date,valor:sla};
        ar.push(aux);
	return ar;
}

//open = real
function getReal(d){
	var ar=[];
	var aux={date:"",real:""};
	for(i=0;i<d.length;i++){
		if(d[i].real!="null"){
                        //alert("nulo nao " +d[i].real + " "+ typeof d[i].real);
			aux.date=d[i].date;
			aux.real=d[i].real;
			ar.push(aux);
		}else{
                    //alert("nulao "+d[i].real+ " "+ typeof d[i].real);
                }
		aux={date:"",real:""};
	}
        //alert("real tem:"+ar);
	return ar;
}

//close = previsto
function getPrevisto(d){
	var ar=[];
	var aux={date:"",real:""};
	for(i=0;i<d.length;i++){
		if(d[i].previsto!="null"){
                        //alert("nulo nao "+d[i].previsto);
			aux.date=d[i].date;
			aux.previsto=d[i].previsto;
			ar.push(aux);
		}else{
                    //alert("nulao "+d[i].previsto);
                }                
		aux={date:"",previsto:""};
	}
        //alert("previsto tem:"+ar);
	return ar;
}

function plotaGrafico(){
    desenhaGrafico();
    window.setTimeout(plotaGrafico, 60000);
}

function desenhaGrafico(){
        $("svg").empty();
	$("#legenda").empty();
	// set the dimensions and margins of the graph
	var widthSvg = $("svg").width();
	var heightSvg = widthSvg*0.6;
	if($( window ).width()>400){
		var margin = {top: 10, right: 15, bottom: 25, left: 55};
	}else{
		var margin = {top: 10, right: 10, bottom: 25, left: 35};
	}
	var width = widthSvg - margin.left - margin.right;
	var height = heightSvg - margin.top - margin.bottom;

	// parse the date / time
	var parseTime = d3.timeParse("%d-%m-%Y-%H:%M");

	// set the ranges
	var x = d3.scaleTime().range([0, width]);
	var y = d3.scaleLinear().range([height, 0]);

	// define the 1st line
	var valueline = d3.line()
	    .x(function(d) { return x(d.date); })
	    .y(function(d) { return y(d.previsto); });//LEMBRAR

	// define the 2nd line
	var valueline2 = d3.line()
	    .x(function(d) { return x(d.date); })
	    .y(function(d) { return y(d.real); });//LEMBRAR

	if(slaValue!=null){
		var slaline = d3.line()
		    .x(function(d) { return x(d.date); })
		    .y(function(d) { return y(d.valor); });
	}
	// append the svg obgect to the body of the page
	// appends a 'group' element to 'svg'
	// moves the 'group' element to the top left margin
	var svg = d3.select("svg")
	    .attr("width", width + margin.left + margin.right)
	    .attr("height", height + margin.top + margin.bottom)
	  .append("g")
	    .attr("transform",
	          "translate(" + margin.left + "," + margin.top + ")");

	// Get the data
	d3.csv(pathConsulta, function(error, data) {
	  	if (error!=null){
                    escondebts();
                }else{
                    preencheReplicas();
                    mostrabts();
                }
                data = checaDadosok(data);
                // format the data
	  	data.forEach(function(d) {
	      	d.date = parseTime(d.date);
	  	});
	  	// Scale the range of the data
	  	x.domain(d3.extent(data, function(d) { return d.date; }));
	  	y.domain([0, d3.max(data, function(d) {
		  if(slaValue==null){
		  	return Math.max(d.previsto, d.real)+50;
		  }else{
		  	return Math.max(d.previsto, d.real,slaValue)+50;
		  } 
		})]);

	  	realData = getReal(data);
	  	previstoData = getPrevisto(data);
                /*if(previstoData.length==0 && realData.length>1){//ajuste
                    var inser = {date:realData[0].date,previsto:realData[0].real};
                    previstoData.push(inser);
                    alert(inser.date +" "+inser.previsto);
                    inser = {date:realData[1].date,previsto:realData[1].real};
                    previstoData.push(inser);
                    alert(inser.date +" "+inser.previsto);
                }
                alert(previstoData.length +" "+realData.length);*/
                
	  	if(slaValue!=null){
	  		sladata = getSla(realData,previstoData,slaValue);
	  	}
	  	// Add the valueline path.
	  	svg.append("path")
	    	.data([previstoData])
	    	.attr("class", "line")
	      	.style("stroke", "gray")
	      	.attr("d", valueline);

	  	// Add the valueline2 path.
	  	svg.append("path")
	    	.data([realData])
	      	.attr("class", "line")
	      	.style("stroke", "green")
	      	.attr("d", valueline2);

	  	if(slaValue!=null){
	  		svg.append("path")
		    	.data([sladata])
		    	.attr("class", "line")
		    	.style("stroke", "red")
		    	.attr("d", slaline)
	  	}

	  	// Add the X Axis
	  	svg.append("g")
	    	.attr("transform", "translate(0," + height + ")")
	    	.call(d3.axisBottom(x)
            	.ticks(5));

	  	// Add the Y Axis
	  	svg.append("g")
	    	.call(d3.axisLeft(y)
	      		.ticks(5))
	    	.append("text")
	        .attr("transform", "rotate(-90)")
	        .attr("y", 6)
	        .attr("dy", "0.71em")
	        .attr("fill", "#000")
	        .text("Tempo de resposta (milis)");

	    if(slaValue==null){
	    	$("#legenda").append("<span id='real'>Real</span><span id='previsto'>Previsto</span>");
	    }else{
	    	$("#legenda").append("<span id='real'>Real</span><span id='previsto'>Previsto</span><span id='sla'>SLA</span>");
	    	alertaQuebraSLA(realData,previstoData);
	    }
	});
}

function inserirSla(){
    var valorNovo;
    if(slaValue==null){
        promptNumerico("Insira um novo valor de SLA","");
    }else{
        promptNumerico("Insira um novo valor de SLA",slaValue);
    }
    /*if(valorNovo.replace(" ","")==""){
        valorNovo="a";
    }
    valorNovo = Number(valorNovo);
    if(isNaN(valorNovo)){
        alertaErro("Erro","Não foi possível salvar o novo valor para SLA");
	//inserirSla;
    }else{
        slaValue=valorNovo;
        plotaGrafico();
        $("#btnExSla").prop('disabled', false);
        $("#btnInSla").empty();
        $("#btnInSla").append("Editar SLA");
    }*/
}

function excluirSla(){
    swal({
        title: 'Excluir SLA',
        text: "Deseja mesmo excluir o SLA existente?",
        type: 'warning',
        showCancelButton: true,
        cancelButtonText: 'Cancelar',
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sim'
    }).then(function () {
        slaValue=null;
        $("#btnExSla").prop('disabled', true);
        $("#btnInSla").empty();
        $("#btnInSla").append("Adicionar SLA");
        plotaGrafico();
    });
    /*var vai = confirm("Deseja mesmo excluir o SLA existente?");
	if(vai!=null){
		if(vai){
                    slaValue=null;
                    $("#btnExSla").prop('disabled', true);
                    $("#btnInSla").empty();
                    $("#btnInSla").append("Adicionar SLA");
                    plotaGrafico();
		}
	}*/
}
function mostrabts(){
    $("#btnInSla").css('display', 'inherit');
    $("#btnExSla").css('display', 'inherit');
    $(".textoseminfo").css('display', 'none');
}
function escondebts(){
    $("#btnInSla").css('display', 'none');
    $("#btnExSla").css('display', 'none');
    $(".textoseminfo").css('display', 'inherit');
}
function preencheReplicas(){
    $("#gruporeplicas").empty();
    var jqxhr = $.getJSON( "/CloudBViewer/replicasdisponiveis?db="+dbname, function(data) {
        $.each(data,function(key,val){
            $("#gruporeplicas").append('<li class="list-group-item">'+val.name+'</li>');
            });
        })
        /*.fail(function() {
            alert("Erro, não foi possível se comunicar a base de dados!");
        })*/
}

function alertaAviso(titulo,mensagem){
    swal({
        title: titulo,
        text: mensagem,
        type: 'warning',
        confirmButtonColor: '#3085d6',
        confirmButtonText: 'Ok!'
        });
}
function alertaErro(titulo,mensagem){
    swal({
        title: titulo,
        text: mensagem,
        type: 'error',
        confirmButtonColor: '#3085d6',
        confirmButtonText: 'Ok!'
        });
}
function promptNumerico(titulo,iv){
    var retorno = 0;
    swal({
    title: titulo,
    input: 'number',
    showCancelButton: true,
    confirmButtonText: 'Confirmar',
    inputValue: iv,
    cancelButtonText: 'Cancelar',
    showLoaderOnConfirm: true,
    allowOutsideClick: false
    }).then(function (valorNovo) {
        if(valorNovo.replace(" ","")==""){
            valorNovo="a";
        }
        valorNovo = Number(valorNovo);
        if(isNaN(valorNovo)){
            alertaErro("Erro","Não foi possível salvar o novo valor para SLA");
            //inserirSla;
        }else{
            slaValue=valorNovo;
            plotaGrafico();
            $("#btnExSla").prop('disabled', false);
            $("#btnInSla").empty();
            $("#btnInSla").append("Editar SLA");
        }
    });
    return retorno;
}

function checaDadosok(data){
    if(data.length>0){
        var i = 0;
        var c = false;
        while(i<data.length){
            if(data[i].previsto!="null"){
              c=true;  
            }
            i++;
        }
        if(!c){
            data[0].previsto=data[0].real;
        }
    }
    return data;
}