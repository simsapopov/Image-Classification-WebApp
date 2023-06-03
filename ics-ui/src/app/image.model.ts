export interface Image {
    id: number;
    tags: Tag[];
    name: string;
    url: string;
    imgurlUrl: string;
    analyzedAt: Date;
    width: number;
    height: number;
  }
  
  export interface Tag {
    id: number;
    tag: string;
    confidencePercentage: number;
  }
  
  export interface ImagePage {
    content: Image[];
    pageable: {
      sort: {
        sorted: boolean;
        unsorted: boolean;
        empty: boolean;
      };
      pageNumber: number;
      pageSize: number;
      offset: number;
      paged: boolean;
      unpaged: boolean;
    };
    totalElements: number;
    totalPages: number;
    last: boolean;
    number: number;
    size: number;
    sort: {
      sorted: boolean;
      unsorted: boolean;
      empty: boolean;
    };
    numberOfElements: number;
    first: boolean;
    empty: boolean;
  }
  