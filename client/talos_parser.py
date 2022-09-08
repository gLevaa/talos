import sys
import json
from enum import Enum

# TODO: Cleanup, organise into classes

# We simply print output, read by Java program

# Key acts as index to dict mapping to parse func, see PostParser class
class PostType(Enum):
    TEXT = 0
    CROSSPOST = 1
    INTERNAL_IMAGE = 2
    INTERNAL_VIDEO = 3
    EXTERNAL_IMAGE = 4
    EXTERNAL_VIDEO = 5
    EXTERNAL_LINK = 6


def parse_source(data):
    data = data["data"]
    extracted_urls = {"urls":[], "status":"success"}

    try:
        next = data["after"]
        post_table = data["children"]

        for post in post_table:
            post_data = post["data"]

            post_id = post_data["name"]
            subreddit = post_data["subreddit_name_prefixed"]

            extracted_urls["urls"].append(f"https://old.reddit.com/{subreddit}/comments/{post_id[3:]}/.json")

        extracted_urls["source"] = f"https://old.reddit.com/r/{subreddit}/new/?count={int(sys.argv[2]) + 25}&after={next}"
    except Exception as e:
        extracted_urls = {"status": "failure", "error": str(e)}

    print(extracted_urls)
    

class PostParser:
    def __init__(self, data):
        self.post_data = data[0]["data"]["children"][0]["data"]
        #comment_data = data[1]["data"]

        self.parsed = {}
    
    def parse(self):
        post_type = self.determine_post_type()
        self.parse_generalised_fields()

        parse_types = {
            PostType.TEXT: self.parse_text,
            PostType.CROSSPOST: self.parse_crosspost,
            PostType.INTERNAL_IMAGE: self.parse_image,
            PostType.EXTERNAL_IMAGE: self.parse_image,
            PostType.INTERNAL_VIDEO: self.parse_video,
            PostType.EXTERNAL_VIDEO: self.parse_video,
            PostType.EXTERNAL_LINK: self.parse_link,
        }

        is_internal = post_type in {PostType.INTERNAL_IMAGE, PostType.INTERNAL_VIDEO}
        parse_types[post_type](is_internal)

    def parse_generalised_fields(self):
        pass

    def parse_text(self):
        print("text")

    def parse_crosspost(self):
        print("crosspost")

    def parse_image(self, is_internal):
        print(f"image, is_internal={str(is_internal)}")

    def parse_video(self, is_internal):
        print(f"video, is_internal={str(is_internal)}")

    def parse_link(self):
        print("external link")

    def determine_post_type(self):
        # https://developer.mozilla.org/en-US/docs/Web/Media/Formats/Image_types
        image_extensions = [".apng", ".avif", ".gif", ".gifv", ".jpg", ".jpeg", ".jfif", ".pjpeg",\
            ".pjp", ".png", ".svg", ".webp", ".bmp", ".ico", ".cur", ".tif", ".tiff"]
        
        url                 = self.post_data["url"]
        media               = self.post_data["media"]
        domain              = self.post_data["domain"]

        # domain can be null if post is deleted, lazy evaluate
        domain_is_self      = self.post_data["is_self"] or f"self.{self.post_data['subreddit']}" in self.post_data["domain"]
        is_self             = self.post_data["is_self"]
        contains_crosspost  = "crosspost_parent_list" in self.post_data
        contains_gallery    = "gallery_data" in self.post_data
        
        if is_self or domain_is_self:   return PostType.TEXT
        elif contains_crosspost:        return PostType.CROSSPOST
        elif "i.redd.it" in domain:     return PostType.INTERNAL_IMAGE
        elif contains_gallery:          return PostType.INTERNAL_IMAGE
        elif "v.redd.it" in domain:     return PostType.INTERNAL_VIDEO
        
        if any(extension in url for extension in image_extensions) or ("gfycat" in url):
            return PostType.EXTERNAL_IMAGE
        elif media == None:
            return PostType.EXTERNAL_LINK
        else:
            return PostType.EXTERNAL_VIDEO


def main():
    f = open('page.json')
    data = json.load(f)

    is_source = True if sys.argv[1] == "s" else False

    if is_source:
        parse_source(data)
    else:
        parser = PostParser(data)
        parser.parse()

    f.close()

main()