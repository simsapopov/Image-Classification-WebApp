package com.example.ics.service;

import com.clarifai.channel.ClarifaiChannel;
import com.clarifai.credentials.ClarifaiCallCredentials;
import com.clarifai.grpc.api.*;
import com.clarifai.grpc.api.status.StatusCode;
import com.example.ics.entity.Images;
import com.example.ics.entity.Tag;
import com.example.ics.reposittory.ImagesRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClarifaiService {
    private final ImagesService imagesService;
    private final TagService tagService;
    private final ImgurService imgurService;
    private final ImagesRepository imagesRepository;
    private final ThrottleService throttleService;
    private final CheckSum checkSum;

    public String classifyImageWithClarifai(String jsonString) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonString);

        String imageUrl = jsonObject.getString("imageUrl");
        String imageHash = checkSum.getChecksum(imageUrl);
        Images image = imagesRepository.findByHash(imageHash);
        if (image != null) {

            return image.getId().toString();

        }
        String ImgurUrl = imgurService.uploadImage(imageUrl);
        image = imagesService.findImageByUrl(imageUrl);
        if (image != null) {

            return image.getId().toString();
        }

        if (throttleService.shouldThrottle()) {

            return "Rate limit exceeded. Please try again later.";
        }

        image = imagesService.saveImage(ImgurUrl, imageUrl, imageHash);
        List<Tag> tagList = getTagsListClarifai(image);
        tagService.addTags(tagList, image);
        image.setTags(tagList);
        imagesRepository.saveAndFlush(image);
        return image.getId().toString();

    }

    public List<Tag> getTagsListClarifai(Images image) {
        image.setName("Clarifai");
        V2Grpc.V2BlockingStub stub = V2Grpc.newBlockingStub(ClarifaiChannel.INSTANCE.getGrpcChannel())
                .withCallCredentials(new ClarifaiCallCredentials("edc51e099d9f405e8d0ee69b1aa1ee57"));
        MultiOutputResponse postModelOutputsResponse = stub.postModelOutputs(
                PostModelOutputsRequest.newBuilder()
                        .setUserAppId(UserAppIDSet.newBuilder().setUserId("clarifai").setAppId("main"))
                        .setModelId("general-image-recognition")
                        .addInputs(
                                Input.newBuilder().setData(
                                        Data.newBuilder().setImage(
                                                Image.newBuilder().setUrl(image.getImgurlUrl())
                                        )
                                )
                        )
                        .build()
        );

        if (postModelOutputsResponse.getStatus().getCode() != StatusCode.SUCCESS) {
            throw new RuntimeException("Post model outputs failed, status: " + postModelOutputsResponse.getStatus());
        }

        Output output = postModelOutputsResponse.getOutputs(0);


        List<Tag> tags = new ArrayList<>();
        for (Concept concept : output.getData().getConceptsList()) {
            Tag tag = new Tag();
            tag.setTag(concept.getName());
            tag.setConfidencePercentage(concept.getValue() * 100);
            tag.setImage(image);
            tags.add(tag);
        }
        return tags;

    }

}
