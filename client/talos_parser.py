import sys
import json

def parse_source(data):
    extracted_urls = {"urls":[], "status":"success"}

    try:
        next = data["after"]
        post_table = data["children"]

        for post in post_table:
            post_data = post["data"]

            post_id = post_data["name"]
            subreddit = post_data["subreddit_name_prefixed"]

            extracted_urls["urls"].append(f"https://old.reddit.com/{subreddit}/comments/{post_id[3:]}/.json")

        extracted_urls["source"] = f"https://old.reddit.com/r/newzealand/new/.json?count={int(sys.argv[2]) + 25}&after={next}"
    except:
        extracted_urls = {"status":"failure"}

    print(extracted_urls)

def parse_page(data):
    pass


f = open('page.json')
data = json.load(f)["data"]

is_source = True if sys.argv[1] == "s" else False
parse_source(data) if is_source else parse_page(data)

f.close()
