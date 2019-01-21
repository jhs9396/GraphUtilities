
;(function(){
    // todo : 아래 url 환경에 맞게 수정 하시오
    const RestServerUrl = 'http://localhost:8080/api/graph'
    var mainG, allEdge, allNode, link, node, force, drag;
    var _W = 1024;
    var _H = 768;

    // todo : 노드나 엣지의 색을 변경하려면 이곳을 수정하세요
    nodeColor = d3.scale.category20()
    linkColor = d3.scale.category20b()

    const MaxNodeSize = 35;
    const MaxEdgeSize = 5;
    nodeSize = d3.scale.linear().domain([0, 10000]).range([20,MaxNodeSize]).clamp(true);
    lineSize = d3.scale.linear().domain([0, 10000]).range([1,MaxEdgeSize]).clamp(true);

    function getPointAtLengthMinusNodeRadius(pathstring, radius, lineWidth) {
        var p = document.createElementNS('http://www.w3.org/2000/svg', 'path');
        p.setAttribute('d', pathstring);
        var totLen = p.getTotalLength();

        var l = totLen > radius ? totLen - radius - lineWidth*4 - 1 : totLen
        return p.getPointAtLength(Math.abs(l));
    }

    function init(data){
        var svg = d3.select('svg')
            .attr('width',_W)
            .attr('height',_H);

        svg.append('rect')
            .attr('x',0)
            .attr('y',0)
            .attr('width',_W)
            .attr('height',_H)
            .attr('fill','white')

        mainG = svg.append('g').attr('class','main')

        allEdge = mainG.append('g').attr('class','allEdge')
        allNode = mainG.append('g').attr('class','allNode')

        var zoom = d3.behavior.zoom().on('zoom',function(){
            mainG.attr('transform', `scale(${d3.event.scale}) translate(${d3.event.translate[0]}, ${d3.event.translate[1]} )`)
        })
        svg.call(zoom);

        document.querySelector('button').addEventListener('click',function (ev) {
            showMeta();
        })
    }

    function dragstart(d) {
        d3.event.sourceEvent.stopPropagation();
        d3.select(this).classed("fixed", d.fixed = true);
    }

    function showMeta() {
        // todo : http://bl.ocks.org/jhb/5955887 참고
        // todo : rest api endpoint 를 환경에 맞게 수정하세요
        // todo : data 가져오는 방법자체를 sherpa에 맞게 수정하세요
        // line 65 를 주석으로 막고 line 66을 풀면 서버없이 동작하는것을 볼수 있습니다 ( 'file://...' url 로 열면 동작안함!!!! )
        d3.json(RestServerUrl + '/d3', function(err,metaGraph){
            // d3.json('graph.json', function(err,metaGraph){
            embedded = metaGraph.embedded
            console.log('data', metaGraph);

            force = d3.layout.force()
                .nodes(embedded.nodes)
                .links(embedded.links.map(l=>{
                    l.source = embedded.nodes.findIndex(n=>n.id === l.source)
            l.target = embedded.nodes.findIndex(n=>n.id === l.target)
            return l;
        }))
        .size([_W, _H])       // 화면크기
                .linkStrength(0.05)
                .friction(0.9)
                .linkDistance(MaxNodeSize*4)
                .charge(-300)
                .gravity(0.01)
                .theta(0.4)
                .alpha(0.1)
                .on('tick',function(){
                    link.select('path')
                        .attr("d", d=>{
                        var targetPoint = getPointAtLengthMinusNodeRadius(
                            `M${d.source.x},${d.source.y} L${d.target.x},${d.target.y}`,
                            nodeSize(d.target.count),
                            lineSize(d.edgecount))
                        return `M${d.source.x},${d.source.y} L${targetPoint.x},${targetPoint.y}`
                    })
                    link.select('text').attr('transform', function(d){
                        if (d.target.x < d.source.x) {
                            var bbox = this.getBBox();

                            rx = bbox.x + bbox.width / 2;
                            ry = bbox.y + bbox.height / 2;
                            return 'rotate(180 ' + rx + ' ' + ry + ')';
                        }else {
                            return 'rotate(0)';
                        }
                    })

                    node.attr("transform", d=>`translate(${d.x}, ${d.y})`)
                })
                .start();

            drag = force.drag()
                .on("dragstart", dragstart)
                .on("drag", function(){
                    d3.event.sourceEvent.stopPropagation()
                });

            var node = allNode.selectAll(".node")
                .data(embedded.nodes, (d)=>d.id)
        .call(drawNode)

            var link = allEdge.selectAll(".link")
                .data(embedded.links, (d)=>d.source.id + ':' + d.target.id)
        .call(drawLink)
        })
    }

    function drawLink(sel) {
        sel.exit().remove();

        var grp = sel.enter().append("g").attr("class", "link")

        grp.append("path")
            .attr("id", (d,i)=> 'edgepath_' + i)
    .attr("fill", d=>linkColor(d.edge))
    .attr("marker-end", "url(#arrowhead)")
            .attr("stroke", d=>linkColor(d.edge))
    .attr("stroke-width", d=>lineSize(d.edgecount))

        var text = grp.append('text')
            .attr("id", (d,i)=> 'edgepath_' + i)

        text.append('textPath')
            .attr('xlink:href', (d,i)=> '#edgepath_' + i)
    .style("text-anchor", "middle")
            .attr("startOffset", "50%")
            .text(d=>d.edge)
    }


    function drawNode(sel) {
        sel.exit().remove();
        var grp = sel.enter().append("g").attr("class", "node")

        grp.call(drag)

        grp.append("circle")
            .attr("r", d=> nodeSize(d.count))
    .attr("stroke-width", 0)
            .style("fill", d=>nodeColor(d.id))


        grp.append('text')
            .text(d=>d.id)
    .attr('text-anchor',"middle")
            .attr('alignment-baseline',"middle")
    }

    init();
})();