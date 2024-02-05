$(document).ready(function(){
    var url;
    var redirectUrl;
    var target;

    $('body').append(`
            <div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4 class="modal-title" id="myModalLabel">CMS Shopping Cart</h4>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        </div>
                        <div class="modal-body delete-modal-body">
                            
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal" id="cancel-delete">Cancel</button>
                            <button type="button" class="btn btn-danger" id="confirm-delete">Delete</button>
                        </div>
                    </div>
                </div>
            </div>`);

    //Delete Action
    $(".delete").on('click',(e)=>{ // when the delete button from the ui with a class of 'delete' is clicked 
        e.preventDefault();

        target = e.target;
        var Id = String($(target).data('id'));
        var controller = $(target).data('controller'); // /admin/pages/
        var action = $(target).data('action'); // delete
        var bodyMessage = $(target).data('body-message');
        redirectUrl = $(target).data('redirect-url');
        // /admin/pages/delete/1 
        url = controller + action + "/" +Id;
        $(".delete-modal-body").text(bodyMessage);
        $("#deleteModal").modal('show');
    });

    $("#confirm-delete").on('click',()=>{
        $.ajax({
            url: url,
            method: "GET",
            success: function(){
                //$('.alert').addClass('alert-success').html('Page deleted.');
                window.location.href = redirectUrl;
            },
            error: function (){
                console.log('error deleting');
            }
        })
    });

    /* 
    .ck .ck-content {
        height: 200px;
    }
    */
    // CK editor - Please use the CDN link for CKEditor 5
    if($("#content").length){
        ClassicEditor.create(document.querySelector("#content")).catch(error => {
            console.log(error);
        })
    }
    if($("#description").length){
        ClassicEditor.create(document.querySelector("#description")).catch(error => {
            console.log(error);
        })
    }

    // add the product to the cart 
    $("a.addToCart").click(function (e){
        e.preventDefault();
        let $this = $(this);
        $this.next().removeClass('d-none');
        let id = $this.attr("data-id");
        let url = "/cart/add/" + id;
        $.get(url, {}, function(data){
            $("div.cart").html(data);
        }).done(function() {
            $this.parent().parent().find("div.productAdded").fadeIn();
            $this.next().addClass('d-none');
            setTimeout(() => {
                $this.parent().parent().find("div.productAdded").fadeOut();
            }, 1000);
        });
    })

    // cart checkout 
    $("a.checkout").click(function (e){
        e.preventDefault();
        $("div.overlay").removeClass('d-none');
        $.get("/cart/clear", {}, function(){});
        $("form#paypalform").submit();
    })

})