<?php 
    include 'config/db_config.php';
    $data = file_get_contents("php://input");
    $request = json_decode($data);
    $response = array();
    $isValidRequest = false;
    if(isset($request->{'action'})){
        if($request->{'action'} == 'REGISTER_USER'){
            $isValidRequest = true;
            $userName = $request -> {'userName'};
            $query = "INSERT INTO user(name) values('".$userName."')";
            $result = mysqli_query($connection,$query);
            if($result){
                $response['userId'] = mysqli_insert_id($connection);
                $response['status'] = true;
                $response['ResponseCode'] = 0;
                $response['message'] = "User registered successfully";
            }
            else{
                $response['status'] = false;
                $response['ResponseCode'] = 102;
                $response['message'] = "Registeration failed";
            }
        }
        if($request->{'action'} == 'ADD_REVIEW'){
            $isValidRequest = true;
            $userId = $request -> {'userId'};
            $title = $request -> {'title'};
            $description = $request -> {'description'};
            $query = "INSERT INTO review(title,description,user_id) values('".$title."','".$description."','".$userId."')";
            $result = mysqli_query($connection,$query);
            if($result){
                $response['reviewId'] = mysqli_insert_id($connection);
                $response['status'] = true;
                $response['ResponseCode'] = 0;
                $response['message'] = "Review added";
            }
            else{
                $response['status'] = false;
                $response['ResponseCode'] = 103;
                $response['message'] = "Failed to upload review";
            }

        }
        if($request->{'action'} == 'GET_REVIEW'){
            $isValidRequest = true;
            $userId = $request -> {'userId'};
            $query = "SELECT r.id as reviewId, u.id as userId, r.date_time as reviewDateTime, u.date_time as userDateTime, r.*, u.* FROM review r INNER JOIN user u on r.user_id = u.id";
            $result = mysqli_query($connection,$query);
            if($result && mysqli_num_rows($result)>0){
                $myReviews = array();
                $allReviews = array();
                while(($row = mysqli_fetch_assoc($result))!=null){
                    $review = array();
                    $review["reviewId"] = $row ['reviewId'];
                    $review["reviewrName"] = $row ['name'];
                    $review["title"] = $row ['title'];
                    $review["description"] = $row ['description'];
                    $review["dateTime"] = $row ['reviewDateTime'];
                    $allReviews[] = $review;
                    if($row['userId'] == $userId){
                        $myReviews[] = $review;
                    }
                }
                $response['status'] = true;
                $response['ResponseCode'] = 0;
                $response['message'] = "Blogs available";
                $response['allReviews'] = $allReviews;
                $response['myReviews'] = $myReviews;
            }
            else{
                $response['status'] = false;
                $response['ResponseCode'] = 104;
                $response['message'] = "No reviews available";
            }

        }
        if($request->{'action'} == 'UPDATE_REVIEW'){
            $isValidRequest = true;
            $userId = $request -> {'userId'};
            $reviewId = $request -> {'reviewId'};
            $title = $request -> {'title'};
            $description = $request -> {'description'};
            $query = "UPDATE review SET title='".$title."',description='".$description."' WHERE id='".$reviewId."'";
            $result = mysqli_query($connection,$query);
            if($result){
                $response['reviewId'] = $reviewId;
                $response['status'] = true;
                $response['ResponseCode'] = 0;
                $response['message'] = "Review updated";
            }
            else{
                $response['status'] = false;
                $response['ResponseCode'] = 105;
                $response['message'] = "Failed to update review";
            }
        }
        if($request->{'action'} == 'DELETE_REVIEW'){
            $isValidRequest = true;
            $userId = $request -> {'userId'};
            $reviewId = $request -> {'reviewId'};
            $query = "DELETE FROM review WHERE id='".$reviewId."'";
            $result = mysqli_query($connection,$query);
            if($result){
                $response['reviewId'] = $reviewId;
                $response['status'] = true;
                $response['ResponseCode'] = 0;
                $response['message'] = "Review deleted";
            }
            else{
                $response['status'] = false;
                $response['ResponseCode'] = 106;
                $response['message'] = "Failed to delete review";
            }
        }

        if(!$isValidRequest){
            $response['status'] = false;
            $response['ResponseCode'] = 101;
            $response['message'] = "Invalid Request";
        }
    }
    else{
        $response['status'] = false;
        $response['ResponseCode'] = 100;
        $response['message'] = "Request action not defined";
    }
    echo json_encode($response);
?>